package kahlua.KahluaProject.domain.post.service;

import jakarta.persistence.EntityManager;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.post.converter.CommentConverter;
import kahlua.KahluaProject.domain.post.entity.Comment;
import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.post.dto.request.CommentsCreateRequest;
import kahlua.KahluaProject.domain.post.dto.response.CommentsCreateResponse;
import kahlua.KahluaProject.domain.post.dto.response.CommentsItemResponse;
import kahlua.KahluaProject.domain.post.dto.response.CommentsListResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.domain.post.repository.comment.CommentRepository;
import kahlua.KahluaProject.domain.post.repository.comment.CommentRepositoryCustom;
import kahlua.KahluaProject.domain.post.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentRepositoryCustom commentRepositoryCustom;
    private final EntityManager em;

    @Transactional
    public CommentsCreateResponse createComment(Long post_id, CommentsCreateRequest commentsCreateRequest, User user) {

        // 선택한 게시글이 존재하는 지 확인
        Post existingPost = postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Comment parentComment = null;
        if (commentsCreateRequest.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentsCreateRequest.getParentCommentId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        }

        Comment comment = CommentConverter.toComment(commentsCreateRequest, existingPost, user, parentComment);
        commentRepository.save(comment);

        CommentsCreateResponse commentsCreateResponse = CommentConverter.toCommentCreateResponse(comment);
        return commentsCreateResponse;
    }

    // 게시글 내의 댓글 전체 조회
    @Transactional
    public CommentsListResponse viewCommentsList(Long post_id) {

        // 선택한 게시글이 존재하는 지 확인
        postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        List<CommentsItemResponse> commentsItemResponses = new ArrayList<>();

        List<Comment> commentList = commentRepositoryCustom.findPureCommentListByPost(post_id);

        for (Comment comment : commentList) {
            CommentsItemResponse commentsItemResponse = CommentConverter.toCommentItemResponse(comment);
            commentsItemResponses.add(commentsItemResponse);
        }

        CommentsListResponse commentsListResponse = CommentConverter.toCommentListResponse(commentsItemResponses);
        return commentsListResponse;
    }

    @Transactional
    public CommentsItemResponse deleteComment(Long post_id, Long comment_id) {

        // 선택한 게시글이 존재하는 지 확인
        postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(comment_id)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        if (comment.getDeletedAt() == null) {
            comment.markAsDeleted();
            commentRepository.save(comment);
        }

        CommentsItemResponse commentsItemResponse = CommentConverter.toCommentItemResponse(comment);
        return commentsItemResponse;
    }
}
