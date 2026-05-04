package kahlua.KahluaProject.domain.post.repository.post;

import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.post.entity.PostLikes;
import kahlua.KahluaProject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

//        boolean existsByPostAndUser(Post);
        Optional<PostLikes> findByPostAndUser(Post post, User user);
}
