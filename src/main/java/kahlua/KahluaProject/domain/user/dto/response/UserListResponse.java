package kahlua.KahluaProject.domain.user.dto.response;

import kahlua.KahluaProject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class UserListResponse {
    private List<UserListOneResponse> content;
    private long pendingCount;
    private long approvedCount;
    private PageInfo pageInfo;

    @Getter
    @Builder
    public static class PageInfo {
        private int totalPages; // 전체 페이지 수
        private boolean hasNext;
    }

    public static UserListResponse of(Page<User> users, long pendingCount, long approvedCount) {

        PageInfo pageInfo = PageInfo.builder()
                .totalPages(users.getTotalPages())
                .hasNext(users.hasNext())
                .build();

        return UserListResponse.builder()
                .content(users.getContent()
                        .stream()
                        .map(UserListOneResponse::from)
                        .toList())
                .pendingCount(pendingCount)
                .approvedCount(approvedCount)
                .pageInfo(pageInfo)
                .build();
    }

}
