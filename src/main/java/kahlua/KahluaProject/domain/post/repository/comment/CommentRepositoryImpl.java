package kahlua.KahluaProject.domain.post.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kahlua.KahluaProject.domain.post.entity.Comment;
import kahlua.KahluaProject.domain.post.entity.QComment;
import kahlua.KahluaProject.domain.post.entity.QPost;
import kahlua.KahluaProject.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kahlua.KahluaProject.domain.post.entity.QComment.comment;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    // postId에 해당하는 댓글 리스트를 페이지 단위로 반환
    public List<Comment> findPureCommentListByPost(Long postId) {
        QComment parentComment = new QComment("parentComment");
        QUser user = QUser.user;
        QPost post = QPost.post;

        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .join(comment.post, post).fetchJoin()
                .join(comment.user, user).fetchJoin()
                .leftJoin(comment.parentComment, parentComment).fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.createdAt.asc())
                .fetch();
    }
}
