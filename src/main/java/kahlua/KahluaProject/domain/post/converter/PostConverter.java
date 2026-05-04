package kahlua.KahluaProject.domain.post.converter;

import kahlua.KahluaProject.domain.post.dto.response.*;
import kahlua.KahluaProject.domain.post.entity.Post;
import kahlua.KahluaProject.domain.post.entity.PostImage;
import kahlua.KahluaProject.domain.post.entity.PostLikes;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.post.dto.request.PostCreateRequest;
import kahlua.KahluaProject.domain.post.dto.request.PostImageCreateRequest;
import kahlua.KahluaProject.domain.post.dto.request.PostUpdateRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostConverter {

    public static Post toPost(PostCreateRequest postCreateRequest, User user) {
        return Post.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .user(user)
                .postType(postCreateRequest.getPostType())
                .build();
    }

    public static Post toPostUpdate(PostUpdateRequest postUpdateRequest, User user) {
        return Post.builder()
                .title(postUpdateRequest.getTitle())
                .content(postUpdateRequest.getContent())
                .user(user)
                .postType(postUpdateRequest.getPostType())
                .build();
    }

    public static PostLikes toPostLikes(Post post, User user) {
        return PostLikes.builder()
                .post(post)
                .user(user)
                .build();
    }

    public static PostCreateResponse toPostCreateResponse(Post post, User user, List<PostImageCreateResponse> imageUrls) {
        return PostCreateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .likes(post.getLikes())
                .imageUrls(imageUrls)
                .postType(post.getPostType())
                .created_at(post.getCreatedAt())
                .updated_at(post.getUpdatedAt())
                .build();
    }

    public static PostUpdateResponse toPostUpdateResponse(Post post, User user, List<PostImageCreateResponse> imageUrls) {
        return PostUpdateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .likes(post.getLikes())
                .imageUrls(imageUrls)
                .postType(post.getPostType())
                .created_at(post.getCreatedAt())
                .updated_at(post.getUpdatedAt())
                .build();
    }

    public static List<PostImage> toPostImage(List<PostImageCreateRequest> postImageCreateRequests, Post post) {
        return Optional.ofNullable(postImageCreateRequests)
                .orElse(Collections.emptyList())
                .stream()
                .map((PostImageCreateRequest imageRequest) -> PostImage.builder()
                        .url(imageRequest.getUrl())
                        .post(post)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<PostImageCreateResponse> toPostImageCreateResponse(List<PostImage> postImage) {
        List<PostImageCreateResponse> imageUrlResponses = postImage.stream()
                .map(postImage1 -> PostImageCreateResponse.builder()
                        .id(postImage1.getId())
                        .url(postImage1.getUrl())
                        .build())
                .collect(Collectors.toList());

        return imageUrlResponses;
    }

    public static PostGetResponse toPostGetResponse(Post post, boolean isLiked) {
        // imageUrls 변환 로직
        List<PostImageGetResponse> getImageUrls = post.getImageUrls().stream()
                .map(postImage2 -> PostImageGetResponse.builder()
                        .id(postImage2.getId())
                        .url(postImage2.getUrl())
                        .build())
                .collect(Collectors.toList());

        return PostGetResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getUser() != null ? post.getUser().getName() : null)
                .profileImageUrl(post.getUser().getProfileImageUrl())
                .likes(post.getLikes())
                .isLiked(isLiked)
                .imageUrls(getImageUrls)
                .created_at(post.getCreatedAt())
                .created_at(post.getUpdatedAt())
                .build();
    }

    public static List<PostGetResponse> toPostListResponse(List<Post> posts) {
        return Optional.ofNullable(posts)
                .orElse(Collections.emptyList())
                .stream()
                .map(post -> PostGetResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .writer(post.getUser() != null ? post.getUser().getName() : null)
                        .likes(post.getLikes())
                        .postType(post.getPostType())
                        .commentsCount(post.getComments().size())
                        .imageUrls(post.getImageUrls().stream()
                                .map(postImage -> PostImageGetResponse.builder()
                                        .id(postImage.getId())
                                        .url(postImage.getUrl())
                                        .build())
                                .collect(Collectors.toList()))
                        .created_at(post.getCreatedAt())
                        .updated_at(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
