package kahlua.KahluaProject.domain.album.repository;

import kahlua.KahluaProject.domain.album.entity.EmojiType;
import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.domain.album.entity.PhotoReaction;
import kahlua.KahluaProject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoReactionRepository extends JpaRepository<PhotoReaction, Long> {

    // 특정 사진에 달린 모든 반응 데이터를 한 번에 가져옴
    List<PhotoReaction> findAllByPhoto(Photo photo);

    @Modifying
    @Query("DELETE FROM PhotoReaction pr WHERE pr.photo IN :photos")
    void deleteAllByPhotoInQuery(@Param("photos") List<Photo> photos);

    // 특정 사진에 대해 특정 유저가 남긴 반응 찾기
    Optional<PhotoReaction> findByPhotoAndUser(Photo photo, User user);

    // 특정 사진의 특정 이모지 누적 개수 구하기
    long countByPhotoAndEmojiType(Photo photo, EmojiType emojiType);
}
