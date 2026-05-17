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
@Table(name = "photo")
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    @Column(nullable = false)
    private String thumbnailUrl; // Presigned URL로 업로드된 썸네일(저화질) S3 URL

    @Column(nullable = false)
    private String imageUrl; // Presigned URL로 업로드된 원본 이미지 S3 URL

    @Column(nullable = false)
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
}