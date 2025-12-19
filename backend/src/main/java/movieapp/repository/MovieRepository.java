package movieapp.repository;

import movieapp.entity.Movie;
import movieapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByOwner(User owner);
}
