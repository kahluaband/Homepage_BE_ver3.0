package kahlua.KahluaProject.domain.album.repository;

import kahlua.KahluaProject.domain.album.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom {

    // 앨범 ID와 사진 ID가 일치하며 삭제되지 않은 사진 조회
    Optional<Photo> findByIdAndAlbumIdAndDeletedAtIsNull(Long id, Long albumId);


}
