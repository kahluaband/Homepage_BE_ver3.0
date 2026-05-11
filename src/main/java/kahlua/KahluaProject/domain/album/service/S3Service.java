package kahlua.KahluaProject.domain.album.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import kahlua.KahluaProject.domain.album.dto.request.PresignedUrlRequest;
import kahlua.KahluaProject.domain.album.dto.response.PresignedUrlResponse;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);

            // 프론트엔드가 이 타입으로 올리겠다고 명시
            generatePresignedUrlRequest.addRequestParameter(Headers.CONTENT_TYPE, file.getFileType());

            // 실제 URL 발급
            URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

            // CDN이 적용된 최종 이미지 URL 생성
            String imageUrl = baseUrl + s3Key;

            // 결과 리스트에 담기
            urlItems.add(PresignedUrlResponse.UrlItem.builder()
                    .presignedUrl(presignedUrl.toString())
                    .imageUrl(imageUrl)
                    .build());
        }

        return PresignedUrlResponse.builder()
                .urlList(urlItems)
                .build();
    }
}
