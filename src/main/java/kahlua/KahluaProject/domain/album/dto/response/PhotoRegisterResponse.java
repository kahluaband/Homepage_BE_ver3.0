package kahlua.KahluaProject.domain.album.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PhotoRegisterResponse {

    @Schema(description = "업로드 완료된 사진 목록")
    private List<UploadedPhoto> uploadedPhotos;

    @Getter
    @Builder
    public static class UploadedPhoto {
        private Long photoId;
        private String thumbnailUrl;
        private String originalUrl;
        private String category;
        private UploaderInfo uploader;

        @Schema(description = "생성 시간", example = "2026-05-08 17:25:45")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
    }
}
