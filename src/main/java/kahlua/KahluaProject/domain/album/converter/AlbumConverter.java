package kahlua.KahluaProject.domain.album.converter;

import kahlua.KahluaProject.domain.album.dto.request.PhotoRegisterRequest;
import kahlua.KahluaProject.domain.album.dto.response.*;
import kahlua.KahluaProject.domain.album.entity.Album;
import kahlua.KahluaProject.domain.album.entity.EmojiType;
import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.domain.album.entity.PhotoReaction;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AlbumConverter {

    public static PhotoItemResponse toPhotoItemResponse(Photo photo) {
        return PhotoItemResponse.builder()
                .photoId(photo.getId())
                .thumbnailUrl(photo.getThumbnailUrl())
                .uploaderName(photo.getUploader().getName())
                .category(photo.getCategory().name())
                .createdAt(photo.getCreatedAt())
                .build();
    }

    public static PhotoListResponse toPhotoListResponse(Album album, List<Photo> photos, Long cursor, boolean hasNext) {
        List<PhotoItemResponse> content = photos.stream()
                .map(AlbumConverter::toPhotoItemResponse)
                .collect(Collectors.toList());

        return PhotoListResponse.builder()
                .albumId(album.getId())
                .albumTitle(album.getTitle())
                .content(content)
                .cursor(cursor)
                .hasNext(hasNext)
                .build();
    }

    public static PhotoDetailResponse toPhotoDetailResponse(Photo photo, List<PhotoReaction> reactions, User currentUser) {

        UploaderInfo uploaderInfo = UploaderInfo.builder()
                .id(photo.getUploader().getId())
                .name(photo.getUploader().getName())
                .term(photo.getUploader().getTerm() + "기")
                .build();

        Map<EmojiType, Long> countMap = reactions.stream()
                .collect(Collectors.groupingBy(PhotoReaction::getEmojiType, Collectors.counting()));

        Set<EmojiType> clickedByMe = reactions.stream()
                .filter(reaction -> reaction.getUser().getId().equals(currentUser.getId()))
                .map(PhotoReaction::getEmojiType)
                .collect(Collectors.toSet());

        List<ReactionItemResponse> reactionResponses = Arrays.stream(EmojiType.values())
                .map(emoji -> ReactionItemResponse.builder()
                        .emojiType(emoji.name())
                        .count(countMap.getOrDefault(emoji, 0L).intValue())
                        .isClicked(clickedByMe.contains(emoji))
                        .build())
                .collect(Collectors.toList());

        return PhotoDetailResponse.builder()
                .photoId(photo.getId())
                .originalUrl(photo.getImageUrl())
                .category(photo.getCategory().name())
                .uploader(uploaderInfo)
                .createdAt(photo.getCreatedAt())
                .reactions(reactionResponses)
                .build();
    }

    public static List<Photo> toPhotoList(PhotoRegisterRequest request, Album album, User currentUser, String baseUrl) {
        String expectedPrefix = "kahlua/albums/" + album.getId() + "/origin/";
        String safeBaseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        return request.getPhotos().stream()
                .map(item -> {
                    if (!item.getS3Key().startsWith(expectedPrefix)) {
                        throw new GeneralException(ErrorStatus.INVALID_IMAGE_PATH);
                    }
                    String originalUrl = safeBaseUrl + item.getS3Key();
                    String thumbnailUrl = originalUrl; // 임시 조치

                    /* 썸네일 조회 임시 조치를 위한 주석 처리
                    int dotIndex = thumbnailUrl.lastIndexOf(".");
                    if (dotIndex != -1) {
                        thumbnailUrl = thumbnailUrl.substring(0, dotIndex) + "_small" + thumbnailUrl.substring(dotIndex);
                    } */

                    return Photo.builder()
                            .imageUrl(originalUrl)
                            .thumbnailUrl(thumbnailUrl)
                            .s3Key(item.getS3Key())
                            .category(item.getCategory())
                            .album(album)
                            .uploader(currentUser)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static PhotoRegisterResponse toPhotoRegisterResponse(List<Photo> savedPhotos) {
        List<PhotoRegisterResponse.UploadedPhoto> uploadedPhotos = savedPhotos.stream()
                .map(photo -> {
                    UploaderInfo uploaderInfo = UploaderInfo.builder()
                            .id(photo.getUploader().getId())
                            .name(photo.getUploader().getName())
                            .term(photo.getUploader().getTerm() + "기")
                            .build();

                    return PhotoRegisterResponse.UploadedPhoto.builder()
                            .photoId(photo.getId())
                            .thumbnailUrl(photo.getThumbnailUrl())
                            .originalUrl(photo.getImageUrl())
                            .category(photo.getCategory().name())
                            .uploader(uploaderInfo)
                            .createdAt(photo.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return PhotoRegisterResponse.builder()
                .uploadedPhotos(uploadedPhotos)
                .build();
    }

    public static ReactionResponse toReactionResponse(
            Long photoId,
            EmojiType requestedEmoji,
            long currentCount,
            boolean isClicked,
            EmojiType previousEmoji,
            Long previousCount
    ) {
        return ReactionResponse.builder()
                .photoId(photoId)
                .emojiType(requestedEmoji)
                .currentCount(currentCount)
                .isClicked(isClicked)
                .previousEmojiType(previousEmoji)
                .previousCount(previousCount)
                .build();
    }
}
