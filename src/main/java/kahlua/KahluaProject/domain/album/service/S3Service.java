package kahlua.KahluaProject.domain.album.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import kahlua.KahluaProject.domain.album.dto.request.PresignedUrlRequest;
import kahlua.KahluaProject.domain.album.dto.response.PresignedUrlResponse;
import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}") // yml에서 가져오기
    private String baseUrl;

    public PresignedUrlResponse getPresignedUrls(Long albumId, PresignedUrlRequest request) {

        // 최대 20개 검증
        if (request.getFiles().size() > 20) {
            throw new GeneralException(ErrorStatus.IMAGE_NOT_UPLOAD);
        }

        List<PresignedUrlResponse.UrlItem> urlItems = new ArrayList<>();

        for (PresignedUrlRequest.FileItem file : request.getFiles()) {
            // 고유한 파일명 생성
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getFileName();
            // S3에 저장될 실제 경로
            String s3Key = "kahlua/albums/" + albumId + "/origin/" + uniqueFileName;

            // URL 만료 시간 설정 (발급 후 2분 동안만 유효)
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime() + (1000 * 60 * 2);
            expiration.setTime(expTimeMillis);

            // Presigned-URL 발급 요청 객체 생성
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                            .withMethod(HttpMethod.PUT) // 클라이언트가 S3에 서버를 거치지 않고 직접 데이터 전송
                            .withExpiration(expiration);

            // 프론트엔드가 이 타입으로 올리겠다고 명시
            generatePresignedUrlRequest.setContentType(file.getFileType());

            // 실제 URL 발급
            URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

            // 결과 리스트에 담기
            urlItems.add(PresignedUrlResponse.UrlItem.builder()
                    .presignedUrl(presignedUrl.toString())
                    .s3Key(s3Key)
                    .build());
        }

        return PresignedUrlResponse.builder()
                .urlList(urlItems)
                .build();
    }

    public void deleteFiles(List<String> s3Keys) {
        if (s3Keys.isEmpty()) return;

        // S3 삭제 요청서 생성
        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket)
                    .withKeys(s3Keys.toArray(new String[0])); // 지울 파일들의 Key 목록 전달
            amazonS3.deleteObjects(deleteObjectsRequest);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.IMAGE_DELETE_FAILED);
        }
    }

    public String getDownloadPresignedUrl(String s3Key, String downloadFileName) {

        // 다운로드 링크 만료 시간 설정(5분)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + (1000 * 60 * 5);
        expiration.setTime(expTimeMillis);

        try {
            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                    .withContentDisposition("attachment; filename=\"" + downloadFileName + "\"");

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, s3Key)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration)
                            .withResponseHeaders(headerOverrides);

            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            log.info("발급된 URL: {}", url.toString());
            return url.toString();

        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.IMAGE_DOWNLOAD_FAILED);
        }
    }

    public void downloadPhotosAsZipStreaming(List<Photo> photos, ZipOutputStream zos) {
        for (Photo photo : photos) {
            try {
                S3Object s3Object = amazonS3.getObject(bucket, photo.getS3Key());
                try (S3ObjectInputStream is = s3Object.getObjectContent()) {

                    // 확장자 추출 및 파일명 생성
                    String extension = "";
                    int dotIndex = photo.getS3Key().lastIndexOf(".");
                    if (dotIndex != -1) {
                        extension = photo.getS3Key().substring(dotIndex);
                    }
                    String fileName = "KAHLUA_PHOTO_" + photo.getId() + extension;

                    // ZIP 엔트리 생성
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zos.putNextEntry(zipEntry);

                    // S3 데이터 -> 서버 메모리 -> 클라이언트 ZIP 스트림으로 즉시 복사
                    StreamUtils.copy(is, zos);
                    zos.closeEntry();
                }
            } catch (Exception e) {
                throw new GeneralException(ErrorStatus.IMAGE_DOWNLOAD_FAILED);
            }
        }
    }
}
