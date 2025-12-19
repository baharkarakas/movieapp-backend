package movieapp.repository;

import movieapp.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {
    @Query("""
    select wi from WatchlistItem wi
    join fetch wi.movie
    join fetch wi.category c
    where c.id = :categoryId and c.isPublic = true
""")
    List<WatchlistItem> findAllPublicByCategoryIdWithDetails(@Param("categoryId") Long categoryId);

    @Query("""
        select wi from WatchlistItem wi
        join fetch wi.category
        join fetch wi.movie
        join wi.user u
        where u.email = :email
    """)
    List<WatchlistItem> findAllByUserEmailWithDetails(@Param("email") String email);

    @Query("""
        select wi from WatchlistItem wi
        join fetch wi.category
        join fetch wi.movie
        join wi.user u
        where u.email = :email and wi.category.id = :categoryId
    """)
    List<WatchlistItem> findAllByUserEmailAndCategoryIdWithDetails(
            @Param("email") String email,
            @Param("categoryId") Long categoryId
    );

    // for delete security check
    Optional<WatchlistItem> findByIdAndUser_Email(Long id, String email);

    // prevent duplicates
    boolean existsByUser_EmailAndCategory_IdAndMovie_Id(String email, Long categoryId, Long movieId);
}
