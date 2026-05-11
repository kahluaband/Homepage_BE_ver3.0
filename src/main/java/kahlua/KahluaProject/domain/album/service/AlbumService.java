package kahlua.KahluaProject.domain.album.service;

import kahlua.KahluaProject.domain.album.converter.AlbumConverter;
import kahlua.KahluaProject.domain.album.dto.request.PhotoRegisterRequest;
import kahlua.KahluaProject.domain.album.dto.response.PhotoDetailResponse;
import kahlua.KahluaProject.domain.album.dto.response.PhotoListResponse;
import kahlua.KahluaProject.domain.album.dto.response.PhotoRegisterResponse;
import kahlua.KahluaProject.domain.album.entity.Album;
import kahlua.KahluaProject.domain.album.entity.Category;
import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.domain.album.entity.PhotoReaction;
import kahlua.KahluaProject.domain.album.repository.AlbumRepository;
import kahlua.KahluaProject.domain.album.repository.PhotoReactionRepository;
import kahlua.KahluaProject.domain.album.repository.PhotoRepository;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.repository.UserRepository;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final PhotoReactionRepository photoReactionRepository;

    public PhotoListResponse getPhotos(Long albumId, Category category, Long cursor, int size) {

        Album album = albumRepository.findByIdAndDeletedAtIsNull(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

        List<Photo> photos = photoRepository.findPhotosByCursorAndCategory(albumId, category, cursor, size);

        boolean hasNext = photos.size() > size;
        if (hasNext) {
            photos.remove(size); // 정해진 응답 규격을 맞추기 위해 초과된 1개 삭제
        }
        Long nextCursor = photos.isEmpty() ? null : photos.get(photos.size() - 1).getId();

        return AlbumConverter.toPhotoListResponse(album, photos, nextCursor, hasNext);
    }

    public PhotoDetailResponse getPhotoDetail(Long albumId, Long photoId, User currentUser) {

        Photo photo = photoRepository.findByIdAndAlbumIdAndDeletedAtIsNull(photoId, albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PHOTO_NOT_FOUND));

        List<PhotoReaction> reactions = photoReactionRepository.findAllByPhoto(photo);

        return AlbumConverter.toPhotoDetailResponse(photo, reactions, currentUser);
    }

    public PhotoListResponse getMyReactedPhotos(Long albumId, Long cursor, int size, User currentUser) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALBUM_NOT_FOUND));

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

        if (request.getPhotos().size() > 20) {
            throw new GeneralException(ErrorStatus.IMAGE_NOT_UPLOAD);
        }

        List<Photo> photos = AlbumConverter.toPhotoList(request, album, currentUser);

        List<Photo> savedPhotos = photoRepository.saveAll(photos);

        return AlbumConverter.toPhotoRegisterResponse(savedPhotos);
    }
}