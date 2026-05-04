package kahlua.KahluaProject.domain.post.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kahlua.KahluaProject.domain.post.converter.PostConverter;
import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.post.entity.PostType;
import kahlua.KahluaProject.domain.post.dto.response.PostGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static kahlua.KahluaProject.domain.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostGetResponse> findPostListByPagination(String postType, String searchWord, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(eqPostType(postType));
        builder.and(likeTitle(searchWord));

        List<Post> posts = queryFactory.select(post)
                .from(post)
                .orderBy(post.createdAt.desc(), post.id.desc())
                .where(post.deletedAt.isNull()
                        .and(builder))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PostGetResponse> postGetResponses = PostConverter.toPostListResponse(posts);

        Long total = queryFactory.select(post.count())
                .from(post)
                .where(post.deletedAt.isNull()
                        .and(builder))
                .fetchOne();

        return new PageImpl<>(postGetResponses, pageable, total != null ? total : 0);
    }

    //postType이 notice인지 kahlua_time인지 확인
    private BooleanExpression eqPostType(String postType) {
        if (postType == null) {
            return post.postType.eq(PostType.valueOf("NOTICE"));
        }

        postType = postType.toUpperCase();
        return post.postType.eq(PostType.valueOf(postType));
    }

    //제목 검색
    private BooleanExpression likeTitle(String searchWord) {
        if (searchWord == null) {
            return null;
        }
        return post.title.contains(searchWord);
    }

    @Override
    public Page<PostGetResponse> findMyPostByUserId(Long user_id, String postType, String searchWord, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (postType != null) {
            builder.and(eqPostType(postType));
        }
        builder.and(likeTitle(searchWord));
        builder.and(post.user.id.eq(user_id));

        List<Post> posts = queryFactory.select(post)
                .from(post)
                .where(post.deletedAt.isNull()
                        .and(builder))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PostGetResponse> postGetResponses = PostConverter.toPostListResponse(posts);

        Long total = queryFactory.select(post.count())
                .from(post)
                .where(post.deletedAt.isNull()
                        .and(builder))
                .fetchOne();

        return new PageImpl<>(postGetResponses, pageable, total != null ? total : 0);
    }
}
