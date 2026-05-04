package kahlua.KahluaProject.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.post.entity.PostType;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.domain.post.dto.response.PostListResponse;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.post.dto.request.PostCreateRequest;
import kahlua.KahluaProject.domain.post.dto.request.PostUpdateRequest;
import kahlua.KahluaProject.domain.post.dto.response.PostCreateResponse;
import kahlua.KahluaProject.domain.post.dto.response.PostGetResponse;
import kahlua.KahluaProject.domain.post.dto.response.PostUpdateResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.post.service.PostSerivce.PostSearchService;
import kahlua.KahluaProject.domain.post.service.PostSerivce.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "게시판", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postService;
    private final PostSearchService postSearchService;

    @PostMapping("/create")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "게시글 작성", description = """
            공지사항/깔브리타임 게시글을 작성합니다.
            공지사항 작성은 어드민만 가능합니다.""")
    public ApiResponse<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest, @AuthenticationPrincipal AuthDetails authDetails) {
        PostCreateResponse postCreateResponse = postService.createPost(postCreateRequest, authDetails.user());
        return ApiResponse.onSuccess(postCreateResponse);
    }

    @PatchMapping("/{post_id}/update")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "게시글 수정", description = """
            게시글 내용을 수정합니다. 이미지의 경우, 기존 이미지를 삭제하고 싶은 경우 빈 리스트로 전달하고 
            기존 이미지를 유지하고 싶은 경우 null 값으로 데이터를 전송합니다.
            공지사항 수정은 어드민만 가능합니다.""")
    public ApiResponse<PostUpdateResponse> updatePost(@PathVariable("post_id") Long post_id, @RequestBody PostUpdateRequest postUpdateRequest, @AuthenticationPrincipal AuthDetails authDetails) {
        PostUpdateResponse postUpdateResponse = postService.updatePost(post_id, postUpdateRequest, authDetails.user());
        return ApiResponse.onSuccess(postUpdateResponse);
    }

    @PostMapping("/{post_id}/create_like")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "좋아요 생성/삭제", description = "게시글의 좋아요 생성/삭제를 진행합니다.")
    public ResponseEntity<?> cratePostLike(@PathVariable("post_id") Long post_id, @AuthenticationPrincipal AuthDetails authDetails) {
        boolean result = postService.createPostLike(authDetails.user(), post_id);
        if (result == true) return ResponseEntity.ok().body(ApiResponse.onSuccess("like_create"));
        else return ResponseEntity.ok().body(ApiResponse.onSuccess("like_delete"));
    }

    @GetMapping("/{post_id}/detail")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "게시글 내용 조회", description = "게시글 내용을 조회합니다.")
    public ApiResponse<PostGetResponse> viewPost(@PathVariable("post_id") Long post_id, @AuthenticationPrincipal AuthDetails authDetails) {
        PostGetResponse postGetResponse = postService.viewPost(post_id, authDetails.user());
        return ApiResponse.onSuccess(postGetResponse);
    }

    @DeleteMapping("{post_id}/delete")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "게시글 삭제", description = "선택한 게시글을 삭제합니다.")
    public ApiResponse<?> deletePost(@PathVariable("post_id") Long post_id, @AuthenticationPrincipal AuthDetails authDetails) {
        postService.delete(post_id);
        return ApiResponse.onSuccess("post delete success");
    }

    @GetMapping("/list")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "게시판 목록 조회", description = "공지사항/깔브리타임 목록을 조회합니다.")
    public ApiResponse<Page<PostGetResponse>> viewPostList(@AuthenticationPrincipal AuthDetails authDetails,
                                                           @RequestParam(value = "post_type", required = false) String searchType,
                                                           @RequestParam(value = "search_word", required = false) String searchWord,
                                                           Pageable pageable) {
        return ApiResponse.onSuccess(postService.viewPostList(searchType, searchWord, pageable));
    }

    @GetMapping("/search")
    @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "게시판 검색 결과 조회 (초성 검색 포함)", description = "공지사항/깔브리타임에서 글 제목을 검색할 경우, 그 결과를 반환합니다.")
    public ResponseEntity<PostListResponse> searchPosts(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam String query,
            @RequestParam(required = false) PostType postType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PostListResponse result = postSearchService.searchPosts(query,postType, page, size);

        return ResponseEntity.ok(result);
    }
}
