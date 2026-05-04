package kahlua.KahluaProject.domain.post.entity;

import jakarta.persistence.*;
import kahlua.KahluaProject.global.base.BaseEntity;
import kahlua.KahluaProject.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 부모 댓글
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    // 자식 댓글 (REMOVE를 포함하지 않으므로 부모 댓글이 삭제되어도 자식 댓글은 그대로 유지된다.)
    @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> childComments = new ArrayList<>();
}
