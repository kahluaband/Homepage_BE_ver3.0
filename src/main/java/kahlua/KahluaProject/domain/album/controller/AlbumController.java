package kahlua.KahluaProject.domain.album.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.album.dto.request.*;
import kahlua.KahluaProject.domain.album.dto.response.*;
import kahlua.KahluaProject.domain.album.entity.Category;
import kahlua.KahluaProject.domain.album.facade.ReactionLockFacade;
import kahlua.KahluaProject.domain.album.service.AlbumService;
import kahlua.KahluaProject.domain.album.service.S3Service;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipOutputStream;

@Tag(name = "공유 앨범", description = "공유 앨범 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final S3Service s3Service;
    private final ReactionLockFacade reactionLockFacade;

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
    @Operation(summary = "사진 업로드", description = "S3 업로드가 완료된 사진들의 정보를 DB에 저장합니다.")
    public ApiResponse<PhotoRegisterResponse> registerPhotos(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @RequestBody PhotoRegisterRequest request
    ) {
        PhotoRegisterResponse response = albumService.registerPhotos(albumId, request, authDetails.user());
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{albumId}/photos")
    @Operation(summary = "사진 삭제", description = "앨범에서 선택한 사진들을 DB와 S3에서 모두 삭제합니다.")
    public ApiResponse<PhotoDeleteResponse> deletePhotos(
            @PathVariable("albumId") Long albumId,
            @RequestBody PhotoDeleteRequest request
    ) {
        PhotoDeleteResponse response = albumService.deletePhotos(albumId, request.getPhotoIds());
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{albumId}/photos/{photoId}/reactions")
    @Operation(summary = "사진 이모지 반응 추가/변경/취소", description = "사진에 이모지 반응을 남깁니다. 이미 같은 이모지를 눌렀다면 취소되고, 다른 이모지로 누르면 변경됩니다.")
    public ApiResponse<ReactionResponse> toggleReaction(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @Parameter(description = "사진 ID", example = "200") @PathVariable("photoId") Long photoId,
            @RequestBody ReactionRequest request
    ) {
        ReactionResponse response = reactionLockFacade.toggleReactionWithLock(albumId, photoId, request, authDetails.user());
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{albumId}/photos/{photoId}/download")
    @Operation(summary = "단일 사진 다운로드", description = "선택한 사진 한 장을 다운로드할 수 있는 Presigned URL을 발급합니다.")
    public ApiResponse<PhotoDownloadResponse> downloadPhoto(
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @Parameter(description = "사진 ID", example = "200") @PathVariable("photoId") Long photoId
    ) {
        PhotoDownloadResponse response = albumService.downloadPhoto(albumId, photoId);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{albumId}/photos/download/batch")
    @Operation(summary = "사진 복수 다운로드 (ZIP)", description = "선택한 여러 사진을 서버를 통해 실시간으로 압축하여 즉시 다운로드합니다.")
    public ResponseEntity<StreamingResponseBody> downloadMultiplePhotosBatch(
            @Parameter(description = "앨범 ID", example = "1") @PathVariable("albumId") Long albumId,
            @RequestBody PhotoDownloadListRequest request
    ) {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String zipFileName = String.format("KAHLUA_다운로드_%s.zip", dateStr);
        String encodedFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        StreamingResponseBody stream = out -> {
            try (ZipOutputStream zos = new ZipOutputStream(out)) {
                albumService.downloadMultiplePhotosStreaming(albumId, request, zos);
            } catch (GeneralException e) {
                throw e;
            } catch (Exception e) {
                throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(stream);
    }
}