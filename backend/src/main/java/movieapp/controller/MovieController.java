package movieapp.controller;

import jakarta.validation.Valid;
import movieapp.dto.MovieRequest;
import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.repository.MovieRepository;
import movieapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public MovieController(MovieRepository movieRepository,
                           UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    private User currentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElseThrow();
    }

    // ✅ READ (only my movies)
    @GetMapping
    public List<Movie> getMyMovies(Authentication auth) {
        return movieRepository.findByOwner(currentUser(auth));
    }

    // ✅ CREATE (logged in)
    @PostMapping
    public ResponseEntity<Movie> create(@Valid @RequestBody MovieRequest req,
                                        Authentication auth) {
        Movie m = new Movie();
        m.setTitle(req.getTitle());
        m.setDirector(req.getDirector());
        m.setYear(req.getYear());
        m.setPosterUrl(req.getPosterUrl());
        m.setOwner(currentUser(auth));

        return ResponseEntity.ok(movieRepository.save(m));
    }

    // ✅ UPDATE (only owner)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody MovieRequest req,
                                    Authentication auth) {
        Movie m = movieRepository.findById(id).orElse(null);
        if (m == null) return ResponseEntity.notFound().build();

        if (!m.getOwner().getEmail().equals(auth.getName())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        m.setTitle(req.getTitle());
        m.setDirector(req.getDirector());
        m.setYear(req.getYear());
        m.setPosterUrl(req.getPosterUrl());

        return ResponseEntity.ok(movieRepository.save(m));
    }

    // ✅ DELETE (only owner)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        Movie m = movieRepository.findById(id).orElse(null);
        if (m == null) return ResponseEntity.notFound().build();

        if (!m.getOwner().getEmail().equals(auth.getName())) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        movieRepository.delete(m);
        return ResponseEntity.ok().build();
    }
}
