package kahlua.KahluaProject.domain.post.dto.response;

import kahlua.KahluaProject.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PostListResponse {
    private List<PostSearchResponse> posts;
    private PageInfo pageInfo;

    @Getter
    @Builder
    public static class PageInfo {
        private int totalPages;
        private boolean hasNext;
    }

    public static PostListResponse of(Page<Post> post) {
        PageInfo pageInfo = PageInfo.builder()
                .totalPages(post.getTotalPages())
                .hasNext(post.hasNext())
                .build();

        return PostListResponse.builder()
                .posts(post.getContent()
                        .stream()
                        .map(PostSearchResponse::new)
                        .toList())
                .pageInfo(pageInfo)
                .build();
    }
}
