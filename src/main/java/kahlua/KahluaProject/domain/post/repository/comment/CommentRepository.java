package kahlua.KahluaProject.domain.post.repository.comment;

import kahlua.KahluaProject.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
