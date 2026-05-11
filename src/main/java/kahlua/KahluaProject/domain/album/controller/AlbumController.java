package kahlua.KahluaProject.domain.album.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.album.dto.request.PhotoRegisterRequest;
import kahlua.KahluaProject.domain.album.dto.request.PresignedUrlRequest;
import kahlua.KahluaProject.domain.album.dto.response.PhotoDetailResponse;
import kahlua.KahluaProject.domain.album.dto.response.PhotoListResponse;
import kahlua.KahluaProject.domain.album.dto.response.PhotoRegisterResponse;
import kahlua.KahluaProject.domain.album.dto.response.PresignedUrlResponse;
import kahlua.KahluaProject.domain.album.entity.Category;
import kahlua.KahluaProject.domain.album.service.AlbumService;
import kahlua.KahluaProject.domain.album.service.S3Service;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공유 앨범", description = "공유 앨범 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final S3Service s3Service;

    @GetMapping("/{albumId}/photos")
    // @CheckUserType(userType = {UserType.KAHLUA, UserType.ADMIN})
    @Operation(summary = "깔루아 공유 앨범 페이지 및 사진 조회", description = "카테고리별 사진 목록을 커서 기반으로 조회합니다.")
    public ApiResponse<PhotoListResponse> getPhotos(
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @Parameter(description = "사진 목록 카테고리별 조회 (없으면 전체 사진)", example = "PERFORMANCE") @RequestParam(value = "category", required = false) Category category,
            @Parameter(description = "스크롤시 버퍼가 걸리는 마지막 사진의 ID", example = "189") @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "한 번에 가져올 사진의 개수", example = "20") @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        PhotoListResponse response = albumService.getPhotos(albumId, category, cursor, size);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{albumId}/photos/{photoId}")
    @Operation(summary = "사진 상세 조회", description = "사진의 원본과 이모지 반응 목록(내 클릭 여부 포함)을 조회합니다.")
    public ApiResponse<PhotoDetailResponse> getPhotoDetail(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @Parameter(description = "사진 ID", example = "200") @PathVariable("photoId") Long photoId
    ) {
        PhotoDetailResponse response = albumService.getPhotoDetail(albumId, photoId, authDetails.user());
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{albumId}/photos/my-reactions")
    @Operation(summary = "내가 반응한 사진 목록 조회", description = "내가 이모지 반응을 남긴 사진들을 커서 기반으로 조회합니다.")
    public ApiResponse<PhotoListResponse> getMyReactedPhotos(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @Parameter(description = "스크롤 버퍼 마지막 사진 ID") @RequestParam(value = "cursor", required = false) Long cursor,
            @Parameter(description = "한 번에 가져올 개수", example = "20") @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        PhotoListResponse response = albumService.getMyReactedPhotos(albumId, cursor, size, authDetails.user());
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{albumId}/photos/presigned-url")
    @Operation(summary = "Presigned URL 발급", description = "S3에 사진을 직접 업로드하기 위한 일회용 URL을 최대 20개 발급받습니다.")
    public ApiResponse<PresignedUrlResponse> getPresignedUrls(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "앨범 ID") @PathVariable("albumId") Long albumId,
            @RequestBody PresignedUrlRequest request
    ) {
        PresignedUrlResponse response = s3Service.getPresignedUrls(albumId, request);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{albumId}/photos")
    @Operation(summary = "사진 등록", description = "S3 업로드가 완료된 사진들의 정보를 DB에 저장합니다.")
    public ApiResponse<PhotoRegisterResponse> registerPhotos(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @RequestBody PhotoRegisterRequest request
    ) {
        PhotoRegisterResponse response = albumService.registerPhotos(albumId, request, authDetails.user());
        return ApiResponse.onSuccess(response);
    }
}