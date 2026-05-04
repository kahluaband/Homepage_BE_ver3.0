package kahlua.KahluaProject.domain.post.repository.comment;

import kahlua.KahluaProject.domain.post.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findPureCommentListByPost(Long postId);
}
