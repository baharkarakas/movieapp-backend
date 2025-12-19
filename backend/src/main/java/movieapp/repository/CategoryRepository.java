package movieapp.repository;

import movieapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // public collections
    List<Category> findByIsPublicTrueOrderByIdDesc();

    // my collections
    List<Category> findByUser_IdOrderByIdDesc(Long userId);

    // ✅ needed by WatchlistController (check ownership by email)
    Optional<Category> findByIdAndUser_Email(Long id, String email);
}
