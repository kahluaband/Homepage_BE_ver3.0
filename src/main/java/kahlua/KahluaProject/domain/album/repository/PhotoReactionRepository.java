package kahlua.KahluaProject.domain.album.repository;

import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.domain.album.entity.PhotoReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoReactionRepository extends JpaRepository<PhotoReaction, Long> {

    // 특정 사진에 달린 모든 반응 데이터를 한 번에 가져옴
    List<PhotoReaction> findAllByPhoto(Photo photo);
}
