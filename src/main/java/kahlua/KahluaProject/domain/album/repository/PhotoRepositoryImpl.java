package kahlua.KahluaProject.domain.album.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kahlua.KahluaProject.domain.album.entity.Category;
import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.domain.album.entity.QPhotoReaction;
import kahlua.KahluaProject.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kahlua.KahluaProject.domain.album.entity.QPhoto.photo;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Photo> findPhotosByCursorAndCategory(Long albumId, Category category, Long cursor, int size) {
        QUser user = QUser.user;

        return queryFactory
                .selectFrom(photo)
                .join(photo.uploader, user).fetchJoin()
                .where(
                        photo.album.id.eq(albumId),
                        categoryEq(category),
                        cursorLt(cursor)
                )
                .orderBy(photo.id.desc())
                .limit(size + 1)
                .fetch();
    }

    @Override
    public List<Photo> findReactedPhotosByCursor(Long albumId, Long userId, Long cursor, int size) {
        QUser user = QUser.user;
        QPhotoReaction photoReaction = QPhotoReaction.photoReaction;

        return queryFactory
                .select(photoReaction.photo)
                .from(photoReaction)
                .join(photoReaction.photo, photo)
                .join(photo.uploader, user).fetchJoin()
                .where(
                        photo.album.id.eq(albumId),
                        photoReaction.user.id.eq(userId), // 현재 로그인한 사용자가 남긴 반응
                        cursorLt(cursor)
                )
                .distinct() // 한 사진에 여러 반응을 남겼을 경우 중복 조회 방지
                .orderBy(photo.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? photo.category.eq(category) : null;
    }

    private BooleanExpression cursorLt(Long cursor) {
        return cursor != null ? photo.id.lt(cursor) : null;
    }
}