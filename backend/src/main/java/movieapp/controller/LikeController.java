package movieapp.controller;


import movieapp.entity.LikedMovie;
import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.repository.LikedMovieRepository;
import movieapp.repository.MovieRepository;
import movieapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikedMovieRepository likedRepo;
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;

    public LikeController(LikedMovieRepository likedRepo, MovieRepository movieRepo, UserRepository userRepo) {
        this.likedRepo = likedRepo;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<Map<String, Object>> myLikes(Authentication auth) {
        String email = auth.getName();
        return likedRepo.findAllByUser_Email(email).stream()
                .map(l -> {
                    Movie m = l.getMovie();
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("movieId", m.getId());
                    map.put("title", m.getTitle());
                    map.put("director", m.getDirector());
                    map.put("year", m.getYear());
                    map.put("posterUrl", m.getPosterUrl());
                    return map;
                })
                .toList();
    }

    @PostMapping("/{movieId}")
    public ResponseEntity<?> like(@PathVariable Long movieId, Authentication auth) {
        String email = auth.getName();

        Movie movie = movieRepo.findById(movieId).orElse(null);
        if (movie == null) return ResponseEntity.status(404).body("Movie not found");

        if (likedRepo.existsByUser_EmailAndMovie_Id(email, movieId)) {
            return ResponseEntity.badRequest().body("Already liked");
        }

        User user = userRepo.findByEmail(email).orElseThrow();

        LikedMovie like = new LikedMovie();
        like.setUser(user);
        like.setMovie(movie);
        likedRepo.save(like);

        return ResponseEntity.ok(Map.of("liked", true, "movieId", movieId));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> unlike(@PathVariable Long movieId, Authentication auth) {
        String email = auth.getName();

        LikedMovie like = likedRepo.findByUser_EmailAndMovie_Id(email, movieId).orElse(null);
        if (like == null) return ResponseEntity.status(404).body("Like not found");

        likedRepo.delete(like);
        return ResponseEntity.ok(Map.of("liked", false, "movieId", movieId));
    }

    @GetMapping("/{movieId}")
    public Map<String, Object> isLiked(@PathVariable Long movieId, Authentication auth) {
        String email = auth.getName();
        boolean liked = likedRepo.existsByUser_EmailAndMovie_Id(email, movieId);
        return Map.of("movieId", movieId, "liked", liked);
    }
}
