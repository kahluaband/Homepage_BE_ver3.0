package kahlua.KahluaProject.domain.album.repository;

import kahlua.KahluaProject.domain.album.entity.Category;
import kahlua.KahluaProject.domain.album.entity.Photo;
import java.util.List;

public interface PhotoRepositoryCustom {
    List<Photo> findPhotosByCursorAndCategory(Long albumId, Category category, Long cursor, int size);

    // 내가 반응한 사진 목록 조회
    List<Photo> findReactedPhotosByCursor(Long albumId, Long userId, Long cursor, int size);
}
