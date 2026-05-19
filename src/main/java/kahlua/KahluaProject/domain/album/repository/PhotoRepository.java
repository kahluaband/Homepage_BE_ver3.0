package kahlua.KahluaProject.domain.album.repository;

import kahlua.KahluaProject.domain.album.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom {

    Optional<Photo> findByIdAndAlbumId(Long id, Long albumId);


}
