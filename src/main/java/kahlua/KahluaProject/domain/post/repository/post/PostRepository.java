package kahlua.KahluaProject.domain.post.repository.post;

import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findByTitleContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "AND p.postType = :postType " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findByTitleContainingIgnoreCaseAndPostType(
            @Param("query") String query,
            @Param("postType") PostType postType,
            Pageable pageable);

    List<Post> findByPostType(PostType postType);
}
