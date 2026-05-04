package kahlua.KahluaProject.domain.post.converter;

import kahlua.KahluaProject.domain.post.entity.Comment;
import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.post.dto.request.CommentsCreateRequest;
import kahlua.KahluaProject.domain.post.dto.response.CommentsCreateResponse;
import kahlua.KahluaProject.domain.post.dto.response.CommentsItemResponse;
import kahlua.KahluaProject.domain.post.dto.response.CommentsListResponse;

import java.util.List;
import java.util.Optional;

public class CommentConverter {

    public static Comment toComment(CommentsCreateRequest commentsCreateRequest, Post existingPost, User user, Comment parentComment) {
        return Comment.builder()
                .post(existingPost)
                .user(user)
                .content(commentsCreateRequest.getContent())
                .parentComment(parentComment)
                .build();
    }

    public static CommentsCreateResponse toCommentCreateResponse(Comment comment) {
        Long parentCommentId = Optional.ofNullable(comment.getParentComment())
                .map(Comment::getId)
                .orElse(null);

        return CommentsCreateResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .user(comment.getUser().getName())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .content(comment.getContent())
                .parentCommentId(parentCommentId)
                .created_at(comment.getCreatedAt())
                .build();
    }

    public static CommentsItemResponse toCommentItemResponse(Comment comment) {
        Long parentCommentId = Optional.ofNullable(comment.getParentComment())
                .map(Comment::getId)
                .orElse(null);

        return CommentsItemResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .user(comment.getUser().getName())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .content(comment.getContent())
                .parentCommentId(parentCommentId)
                .created_at(comment.getCreatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();
    }

    public static CommentsListResponse toCommentListResponse(List<CommentsItemResponse> commentsItemResponses) {
        return CommentsListResponse.builder()
                .comments_count(commentsItemResponses.stream().count())
                .comments(commentsItemResponses)
                .build();
    }
}
