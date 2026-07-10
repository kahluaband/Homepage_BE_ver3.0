package kahlua.KahluaProject.domain.album.repository;

import kahlua.KahluaProject.domain.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByIdAndDeletedAtIsNull(Long id);

    // 특정 기수의 앨범 찾기
    Optional<Album> findByTermAndDeletedAtIsNull(String term);
}