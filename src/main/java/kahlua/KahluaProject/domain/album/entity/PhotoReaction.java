package kahlua.KahluaProject.domain.album.entity;

import jakarta.persistence.*;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.global.base.BaseEntity;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "photo_reaction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_photo_user_reaction",
                        columnNames = {"photo_id", "user_id"}
                )
        }
)
public class PhotoReaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_reaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmojiType emojiType;

    // 이모지 변경 메서드
    public void updateEmoji(EmojiType newEmojiType) {
        this.emojiType = newEmojiType;
    }
}
