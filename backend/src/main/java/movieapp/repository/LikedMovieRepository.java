package movieapp.repository;

import movieapp.entity.LikedMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikedMovieRepository extends JpaRepository<LikedMovie, Long> {

    boolean existsByUser_EmailAndMovie_Id(String email, Long movieId);

    Optional<LikedMovie> findByUser_EmailAndMovie_Id(String email, Long movieId);

    List<LikedMovie> findAllByUser_Email(String email);
}
