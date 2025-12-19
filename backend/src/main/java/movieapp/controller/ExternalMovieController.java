package movieapp.controller;

import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.repository.MovieRepository;
import movieapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/external")
public class ExternalMovieController {

    private final UserRepository userRepo;
    private final MovieRepository movieRepo;
    private final RestTemplate restTemplate;

    // application.properties: app.omdb.api-key=xxxx
    @Value("${app.omdb.api-key:}")
    private String omdbApiKey;

    public ExternalMovieController(UserRepository userRepo, MovieRepository movieRepo) {
        this.userRepo = userRepo;
        this.movieRepo = movieRepo;
        this.restTemplate = new RestTemplate();
    }

    // --- Helpers ---
    private boolean isKeyMissing() {
        return omdbApiKey == null || omdbApiKey.trim().isEmpty();
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String str(Map<?, ?> map, String key) {
        return Objects.toString(map.get(key), "").trim();
    }

    private static int parseYear(String yearRaw) {
        // "2014–2017" gibi gelebilir → ilk 4 haneyi al
        if (yearRaw == null) return 0;
        yearRaw = yearRaw.trim();
        if (yearRaw.length() < 4) return 0;
        try {
            return Integer.parseInt(yearRaw.substring(0, 4));
        } catch (Exception e) {
            return 0;
        }
    }

    // ✅ Public: Search on OMDb
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query) {
        if (isKeyMissing()) {
            return ResponseEntity.badRequest().body(
                    "OMDb API key missing. Set: app.omdb.api-key in application.properties"
            );
        }

        String q = (query == null) ? "" : query.trim();
        if (q.length() < 2) {
            return ResponseEntity.badRequest().body("Query too short.");
        }

        String url = "https://www.omdbapi.com/?apikey=" + enc(omdbApiKey) + "&s=" + enc(q);

        Map<?, ?> res;
        try {
            res = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Failed to reach OMDb: " + e.getMessage());
        }

        if (res == null || !"True".equalsIgnoreCase(str(res, "Response"))) {
            // OMDb Response=False durumunda boş liste dön (UI rahat etsin)
            return ResponseEntity.ok(List.of());
        }

        Object search = res.get("Search");
        if (search == null) return ResponseEntity.ok(List.of());

        // Search alanı zaten List<Map> gibi geliyor
        return ResponseEntity.ok(search);
    }

    // ✅ Requires login: Import a movie by imdbId and save to user's movies
    @PostMapping("/import")
    public ResponseEntity<?> importMovie(@RequestBody Map<String, String> body, Authentication auth) {
        if (isKeyMissing()) {
            return ResponseEntity.badRequest().body(
                    "OMDb API key missing. Set: app.omdb.api-key in application.properties"
            );
        }
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String imdbId = body == null ? "" : Objects.toString(body.get("imdbId"), "").trim();
        if (imdbId.isEmpty()) {
            return ResponseEntity.badRequest().body("imdbId required");
        }

        User me = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = "https://www.omdbapi.com/?apikey=" + enc(omdbApiKey)
                + "&i=" + enc(imdbId) + "&plot=short";

        Map<?, ?> res;
        try {
            res = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Failed to reach OMDb: " + e.getMessage());
        }

        if (res == null || !"True".equalsIgnoreCase(str(res, "Response"))) {
            return ResponseEntity.badRequest().body("Movie not found from OMDb.");
        }

        String title = str(res, "Title");
        String director = str(res, "Director");
        String yearStr = str(res, "Year");
        String poster = str(res, "Poster");

        int year = parseYear(yearStr);

        Movie m = new Movie();
        m.setTitle(title.isEmpty() ? "Unknown" : title);

        if ("N/A".equalsIgnoreCase(director)) director = "";
        m.setDirector(director);

        m.setYear(year == 0 ? 2000 : year);

        if ("N/A".equalsIgnoreCase(poster)) poster = "";
        m.setPosterUrl(poster);

        // IMPORTANT: attach owner (FK)
        m.setOwner(me);

        Movie saved = movieRepo.save(m);
        return ResponseEntity.ok(saved);
    }
}
