package kahlua.KahluaProject.domain.post.repository.post;

import kahlua.KahluaProject.domain.post.dto.response.PostGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostGetResponse> findPostListByPagination(String postType, String searchWord, Pageable pageable);

    Page<PostGetResponse> findMyPostByUserId(Long id, String postType, String searchWord, Pageable pageable);
}
