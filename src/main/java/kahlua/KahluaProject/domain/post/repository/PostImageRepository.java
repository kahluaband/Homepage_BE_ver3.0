package kahlua.KahluaProject.domain.post.repository;

import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    void deleteAllByPost(Post post);
}
