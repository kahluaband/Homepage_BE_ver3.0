package kahlua.KahluaProject.domain.album.service;

import kahlua.KahluaProject.domain.album.converter.AlbumConverter;
import kahlua.KahluaProject.domain.album.dto.request.PhotoDownloadListRequest;
import kahlua.KahluaProject.domain.album.dto.request.PhotoRegisterRequest;
import kahlua.KahluaProject.domain.album.dto.request.ReactionRequest;
import kahlua.KahluaProject.domain.album.dto.response.*;
import kahlua.KahluaProject.domain.album.entity.*;
import kahlua.KahluaProject.domain.album.repository.AlbumRepository;
import kahlua.KahluaProject.domain.album.repository.PhotoReactionRepository;
import kahlua.KahluaProject.domain.album.repository.PhotoRepository;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final PhotoReactionRepository photoReactionRepository;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    public PhotoListResponse getPhotos(Long albumId, Category category, Long cursor, int size, User currentUser) {

        Album album = albumRepository.findByIdAndDeletedAtIsNull(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        validateAlbumAccess(album, currentUser); // 권한 검증

        List<Photo> photos = photoRepository.findPhotosByCursorAndCategory(albumId, category, cursor, size+1);

        boolean hasNext = photos.size() > size;
        if (hasNext) {
            photos.remove(size); // 정해진 응답 규격을 맞추기 위해 초과된 1개 삭제
        }
        Long nextCursor = photos.isEmpty() ? null : photos.get(photos.size() - 1).getId();

        return AlbumConverter.toPhotoListResponse(album, photos, nextCursor, hasNext);
    }

    public PhotoDetailResponse getPhotoDetail(Long albumId, Long photoId, User currentUser) {

        Photo photo = photoRepository.findByIdAndAlbumId(photoId, albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PHOTO_NOT_FOUND));

        validateAlbumAccess(photo.getAlbum(), currentUser); // 권한 검증

        List<PhotoReaction> reactions = photoReactionRepository.findAllByPhoto(photo);

        return AlbumConverter.toPhotoDetailResponse(photo, reactions, currentUser);
    }

    public PhotoListResponse getMyReactedPhotos(Long albumId, Long cursor, int size, User currentUser) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        validateAlbumAccess(album, currentUser); // 권한 검증

        List<Photo> photos = photoRepository.findReactedPhotosByCursor(albumId, currentUser.getId(), cursor, size);

        boolean hasNext = photos.size() > size;
        if (hasNext) {
            photos.remove(size);
        }
        Long nextCursor = photos.isEmpty() ? null : photos.get(photos.size() - 1).getId();

        return AlbumConverter.toPhotoListResponse(album, photos, nextCursor, hasNext);
    }

    @Transactional
    public PhotoRegisterResponse registerPhotos(Long albumId, PhotoRegisterRequest request, User currentUser) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        validateAlbumAccess(album, currentUser); // 권한 검증

        if (request.getPhotos().size() > 20) {
            throw new GeneralException(ErrorStatus.IMAGE_NOT_UPLOAD);
        }

        List<Photo> photos = AlbumConverter.toPhotoList(request, album, currentUser, baseUrl);

        List<Photo> savedPhotos = photoRepository.saveAll(photos);

        return AlbumConverter.toPhotoRegisterResponse(savedPhotos);
    }

    @Transactional
    public PhotoDeleteResponse deletePhotos(Long albumId, List<Long> photoIds, User currentUser) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        validateAlbumAccess(album, currentUser); // 권한 검증

        List<Photo> photos = photoRepository.findAllById(photoIds);

        if (photos.isEmpty()) {
            throw new GeneralException(ErrorStatus.PHOTO_NOT_FOUND);
        }

        boolean isAllInSameAlbum = photos.stream()
                .allMatch(photo -> photo.getAlbum().getId().equals(albumId));

        if (!isAllInSameAlbum) {
            throw new GeneralException(ErrorStatus.PHOTO_NOT_FOUND);
        }

        // S3에서 삭제할 Key 추출
        List<String> s3Keys = photos.stream()
                .map(Photo::getS3Key)
                .toList();

        // S3 파일 삭제
        s3Service.deleteFiles(s3Keys);

        // 이모지 반응 삭제 -> 사진 삭제(in DB)
        photoReactionRepository.deleteAllByPhotoInQuery(photos);
        photoRepository.deleteAllInBatch(photos);

        return PhotoDeleteResponse.builder()
                .deletedPhotoIds(photoIds)
                .deletedCount(photos.size())
                .build();
    }

    @Transactional
    public ReactionResponse toggleReaction(Long albumId, Long photoId, ReactionRequest request, User currentUser) {

        Photo photo = photoRepository.findByIdAndAlbumId(photoId, albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PHOTO_NOT_FOUND));

        validateAlbumAccess(photo.getAlbum(), currentUser); // 권한 검증

        EmojiType requestedEmoji = request.getEmojiType();
        EmojiType previousEmoji = null;
        boolean isClicked;

        // 사용자가 이 사진에 이미 남긴 반응이 있는지 확인
        Optional<PhotoReaction> existingReaction = photoReactionRepository.findByPhotoAndUser(photo, currentUser);

        if (existingReaction.isPresent()) {
            PhotoReaction reaction = existingReaction.get();
            if (reaction.getEmojiType() == requestedEmoji) {
                // 같은 이모지를 눌렀다면 -> 반응 취소 (삭제)
                photoReactionRepository.delete(reaction);
                isClicked = false;
            } else {
                // 다른 이모지를 눌렀다면 -> 새로운 이모지로 변경 (업데이트)
                previousEmoji = reaction.getEmojiType();
                reaction.updateEmoji(requestedEmoji);
                isClicked = true;
            }
        } else {
            // 아직 반응을 남기지 않았다면 -> 새로 생성
            PhotoReaction newReaction = PhotoReaction.builder()
                    .photo(photo)
                    .user(currentUser)
                    .emojiType(requestedEmoji)
                    .build();
            photoReactionRepository.save(newReaction);
            isClicked = true;
        }

        photoReactionRepository.flush();
        long currentCount = photoReactionRepository.countByPhotoAndEmojiType(photo, requestedEmoji);

        Long previousCount = null;
        if (previousEmoji != null) {
            previousCount = photoReactionRepository.countByPhotoAndEmojiType(photo, previousEmoji);
        }

        return AlbumConverter.toReactionResponse(
                photoId, requestedEmoji, currentCount, isClicked, previousEmoji, previousCount
        );
    }

    public PhotoDownloadResponse downloadPhoto(Long albumId, Long photoId, User currentUser) {

        Photo photo = photoRepository.findByIdAndAlbumId(photoId, albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PHOTO_NOT_FOUND));

        validateAlbumAccess(photo.getAlbum(), currentUser); // 권한 검증

        // 확장자 추출 (s3Key에서 마지막 '.' 이후 문자열)
        String s3Key = photo.getS3Key();
        String extension = "";
        int dotIndex = s3Key.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = s3Key.substring(dotIndex); // ex: .jpg, .png
        }

        String fileName = String.format("KAHLUA_PHOTO_%d%s", photoId, extension);

        String downloadUrl = s3Service.getDownloadPresignedUrl(s3Key, fileName);

        return PhotoDownloadResponse.builder()
                .photoId(photoId)
                .fileName(fileName)
                .downloadUrl(downloadUrl)
                .build();
    }

    public void downloadMultiplePhotosStreaming(Long albumId, PhotoDownloadListRequest request, ZipOutputStream zos, User currentUser) { // currentUser 파라미터 추가

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        validateAlbumAccess(album, currentUser); // 권한 검증

        List<Photo> photos = photoRepository.findAllById(request.getPhotoIds());

        if (photos.isEmpty() || photos.size() != request.getPhotoIds().size()) {
            throw new GeneralException(ErrorStatus.PHOTO_NOT_FOUND);
        }

        boolean isAllInSameAlbum = photos.stream()
                .allMatch(photo -> photo.getAlbum().getId().equals(albumId) && photo.getDeletedAt() == null);
        if (!isAllInSameAlbum) {
            throw new GeneralException(ErrorStatus.PHOTO_NOT_FOUND);
        }

        // S3 스트리밍 호출
        s3Service.downloadPhotosAsZipStreaming(photos, zos);
    }

    private void validateAlbumAccess(Album album, User currentUser) {
        // 전체 공유 앨범(ID가 1이거나 term이 null인 경우)은 누구나 접근 가능
        if (album.getId() == 1L || album.getTerm() == null) {
            return;
        }

        // 그 외의 앨범은 '앨범에 설정된 기수'와 '현재 접속한 유저의 기수'가 같아야만 통과
        if (!album.getTerm().equals(currentUser.getTerm())) {
            throw new GeneralException(ErrorStatus.FORBIDDEN_ALBUM_ACCESS);
        }
    }
}