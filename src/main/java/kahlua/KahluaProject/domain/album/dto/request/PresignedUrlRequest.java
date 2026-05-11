package kahlua.KahluaProject.domain.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Getter
public class PresignedUrlRequest {

    @Schema(description = "업로드할 파일 목록 (최대 20개)")
    private List<FileItem> files;

    @Getter
    public static class FileItem {
        @Schema(description = "원본 파일명", example = "image1.jpg")
        private String fileName;

        @Schema(description = "파일 타입", example = "image/jpeg")
        private String fileType;
    }
}
