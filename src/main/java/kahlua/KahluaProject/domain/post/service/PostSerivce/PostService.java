package kahlua.KahluaProject.domain.post.service.PostSerivce;

import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.post.converter.PostConverter;
import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.post.entity.PostImage;
import kahlua.KahluaProject.domain.post.entity.PostLikes;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.domain.post.dto.request.PostCreateRequest;
import kahlua.KahluaProject.domain.post.dto.request.PostUpdateRequest;
import kahlua.KahluaProject.domain.post.dto.response.PostCreateResponse;
import kahlua.KahluaProject.domain.post.dto.response.PostGetResponse;
import kahlua.KahluaProject.domain.post.dto.response.PostImageCreateResponse;
import kahlua.KahluaProject.domain.post.dto.response.PostUpdateResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.domain.post.repository.PostImageRepository;
import kahlua.KahluaProject.domain.post.repository.post.PostLikesRepository;
import kahlua.KahluaProject.domain.post.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kahlua.KahluaProject.domain.post.entity.PostType.NOTICE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikesRepository postLikesRepository;

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest postCreateRequest, User user) {

        // 공지사항인 경우 admin인지 확인
        if (postCreateRequest.getPostType() == NOTICE) {
            if (user.getUserType() != UserType.ADMIN) {
                throw new GeneralException(ErrorStatus.INVALID_USER_TYPE);
            }
        }

        Post post = PostConverter.toPost(postCreateRequest, user);
        postRepository.save(post);

        List<PostImage> imageUrls = PostConverter.toPostImage(postCreateRequest.getImageUrls(), post);
        if (imageUrls.size() > 10) throw new GeneralException(ErrorStatus.IMAGE_NOT_UPLOAD);

        postImageRepository.saveAll(imageUrls);
        List<PostImageCreateResponse> imageUrlResponses = PostConverter.toPostImageCreateResponse(imageUrls);

        PostCreateResponse postCreateResponse = PostConverter.toPostCreateResponse(post, user, imageUrlResponses);

        return postCreateResponse;
    }

    @Transactional
    public PostUpdateResponse updatePost(Long post_id, PostUpdateRequest postUpdateRequest, User user) {

        // 선택한 게시글이 존재하는 지 확인
        Post existingPost = postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 공지사항인 경우 admin인지 확인
        if (postUpdateRequest.getPostType() == NOTICE) {
            if (user.getUserType() != UserType.ADMIN) {
                throw new GeneralException(ErrorStatus.UNAUTHORIZED);
            }
        }

        if (postUpdateRequest.getImageUrls() != null) {
            if (postUpdateRequest.getImageUrls().isEmpty()) {
                existingPost.getImageUrls().clear();
                postImageRepository.deleteAllByPost(existingPost);
            } else {
                List<PostImage> newImages = PostConverter.toPostImage(postUpdateRequest.getImageUrls(), existingPost);
                if (newImages.size() > 10) throw new GeneralException(ErrorStatus.IMAGE_NOT_UPLOAD);

                // 기존 이미지를 DB에서 삭제
                postImageRepository.deleteAllByPost(existingPost);

                // 새로운 이미지 저장
                existingPost.updateImages(newImages);
                postImageRepository.saveAll(newImages);
            }
        }

        existingPost.update(postUpdateRequest.getTitle(), postUpdateRequest.getContent());
        postRepository.save(existingPost);

        List<PostImageCreateResponse> imageUrlResponses = PostConverter.toPostImageCreateResponse(existingPost.getImageUrls());

        PostUpdateResponse postUdpateResponse = PostConverter.toPostUpdateResponse(existingPost, user, imageUrlResponses);

        return postUdpateResponse;
    }

    @Transactional
    public boolean createPostLike(User user, Long post_id) {

        // 선택한 게시글이 존재하는 지 확인
        Post existingPost = postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 사용자가 좋아요를 눌렀는지 확인
        Optional<PostLikes> existingLike = postLikesRepository.findByPostAndUser(existingPost, user);

        if (existingLike.isPresent()) {
            postLikesRepository.delete(existingLike.get());
            System.out.println("like" + existingLike.get());

            existingPost.decreaseLikes();
            postRepository.save(existingPost);
            return false;  // 좋아요 취소
        } else {
            PostLikes newLike = PostConverter.toPostLikes(existingPost, user);
            postLikesRepository.save(newLike);

            existingPost.increaseLikes();
            postRepository.save(existingPost);
            return true;  // 좋아요 생성
        }
    }

    @Transactional
    public PostGetResponse viewPost(Long post_id, User user) {

        // 선택한 게시글이 존재하는 지 확인
        Post existingPost = postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        boolean isLiked = postLikesRepository.findByPostAndUser(existingPost, user).isPresent();

        return PostConverter.toPostGetResponse(existingPost, isLiked);
    }

    @Transactional
    public Page<PostGetResponse> viewPostList(String postType, String searchWord, Pageable pageable) {

        return postRepository.findPostListByPagination(postType, searchWord, pageable);
    }

    @Transactional
    // 마이페이지 내가 작성한 글 리스트 조회
    public Page<PostGetResponse> viewMyPostList(User user, String postType, String searchWord, Pageable pageable) {

        return postRepository.findMyPostByUserId(user.getId(), postType, searchWord, pageable);
    }

    @Transactional
    public void delete(Long post_id) {

        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        postRepository.delete(post);
    }
}