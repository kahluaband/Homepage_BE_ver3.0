package kahlua.KahluaProject.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.post.dto.request.CommentsCreateRequest;
import kahlua.KahluaProject.domain.post.dto.response.CommentsCreateResponse;
import kahlua.KahluaProject.domain.post.dto.response.CommentsItemResponse;
import kahlua.KahluaProject.domain.post.dto.response.CommentsListResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판 댓글", description = "게시판 댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{post_id}/create")
    @Operation(summary = "댓글 작성", description = "게시판 댓글을 작성합니다.")
    public ApiResponse<CommentsCreateResponse> createComment(@PathVariable("post_id") Long post_id, @RequestBody CommentsCreateRequest commentsCreateRequest, @AuthenticationPrincipal AuthDetails authDetails) {
        CommentsCreateResponse commentsCreateResponse = commentService.createComment(post_id, commentsCreateRequest, authDetails.user());
        return ApiResponse.onSuccess(commentsCreateResponse);
    }

    @GetMapping("/{post_id}/list")
    @Operation(summary = "전체 댓글 조회", description = "게시글에 달린 댓글을 조회합니다.")
    public ApiResponse<CommentsListResponse> viewCommentsList(@PathVariable("post_id") Long post_id) {
        CommentsListResponse commentsListResponse = commentService.viewCommentsList(post_id);
        return ApiResponse.onSuccess(commentsListResponse);
    }

    @PatchMapping("/{post_id}/{comment_id}/delete")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public ApiResponse<CommentsItemResponse> deleteComment(@PathVariable("post_id") Long post_id,
                                                           @PathVariable("comment_id") Long comment_id) {
        CommentsItemResponse commentsItemResponse = commentService.deleteComment(post_id, comment_id);
        return ApiResponse.onSuccess(commentsItemResponse);
    }
}
