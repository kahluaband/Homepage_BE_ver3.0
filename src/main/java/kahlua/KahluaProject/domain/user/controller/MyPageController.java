package kahlua.KahluaProject.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.post.dto.response.PostGetResponse;
import kahlua.KahluaProject.domain.reservation.dto.response.ReservationListResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.post.service.PostSerivce.PostService;
import kahlua.KahluaProject.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "마이페이지", description = "마이페이지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/my-page")
public class MyPageController {

    private final ReservationService reservationService;
    private final PostService postService;

    @GetMapping("/reservation")
    @Operation(summary = "사용자 예약내역 조회", description = "사용자의 전체 예약내역을 예약 날짜 내림차순으로 조회합니다." +
            "<br> 같은 날짜에 대해서는 시작 시간 오름차순으로 정렬합니다.")
    public ApiResponse<ReservationListResponse> getReservationList(@AuthenticationPrincipal AuthDetails authDetails) {

        return ApiResponse.onSuccess(reservationService.getByUser(authDetails.user()));
    }

    @DeleteMapping("/reservation/{reservationId}")
    @Operation(summary = "예약내역 삭제", description = "지정한 예약내역을 삭제합니다.")
    public ApiResponse<?> delete(@PathVariable Long reservationId) {

        reservationService.delete(reservationId);
        return ApiResponse.onSuccess("예약내역 삭제 성공");
    }

    @GetMapping("/post/list")
    @Operation(summary = "내가 작성한 글 리스트", description = "내가 작성한 글을 조회합니다.")
    public ApiResponse<Page<PostGetResponse>> viewMyPostList(@AuthenticationPrincipal AuthDetails authDetails,
                                                             @RequestParam(value = "post_type", required = false) String searchType,
                                                             @RequestParam(value = "search_word", required = false) String searchWord,
                                                             Pageable pageable) {
        return ApiResponse.onSuccess(postService.viewMyPostList(authDetails.user(), searchType, searchWord, pageable));
    }
}
