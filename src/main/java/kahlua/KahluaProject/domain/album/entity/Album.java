package kahlua.KahluaProject.domain.album.entity;

import jakarta.persistence.*;
import kahlua.KahluaProject.global.base.BaseEntity;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "album")
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "term")
    private String term;
}