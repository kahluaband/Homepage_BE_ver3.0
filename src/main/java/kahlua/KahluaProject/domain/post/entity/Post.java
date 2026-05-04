package kahlua.KahluaProject.domain.post.entity;

import jakarta.persistence.*;
import kahlua.KahluaProject.global.base.BaseEntity;
import kahlua.KahluaProject.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() where post_id = ?")
@Where(clause = "deleted_at is NULL")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ColumnDefault("0")
    private int likes;  // likes 필드와 연결 필요

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PostImage> ImageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public void addImage(PostImage postImage) {
        postImage = new PostImage(postImage.getUrl(), this);
        ImageUrls.add(postImage);
    }

    public void update(String title, String content) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

    public void updateImages(List<PostImage> newImages) {
        this.ImageUrls.clear(); // 기존 이미지 리스트 비우기
        this.ImageUrls.addAll(newImages);
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }
}
