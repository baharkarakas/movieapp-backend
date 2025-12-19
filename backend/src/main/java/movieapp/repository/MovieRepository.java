package movieapp.repository;

import movieapp.entity.Movie;
import movieapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByOwner(User owner);

    // opsiyonel: imdbId ekleyip duplicate engellemek için
    // boolean existsByOwnerAndImdbId(User owner, String imdbId);
}

