/* package kahlua.KahluaProject.global.config;

import kahlua.KahluaProject.domain.album.entity.Album;
import kahlua.KahluaProject.domain.album.entity.Category;
import kahlua.KahluaProject.domain.album.entity.Photo;
import kahlua.KahluaProject.domain.album.repository.AlbumRepository;
import kahlua.KahluaProject.domain.album.repository.PhotoRepository;
import kahlua.KahluaProject.domain.user.entity.LoginType;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 이미 데이터가 있다면 중복 생성을 막기 위한 방어 로직
        if (photoRepository.count() > 0) {
            return;
        }

        // 1. 테스트용 유저 1명 생성
        User testUser = User.builder()
                .email("test@kahluaband.com")
                .name("테스트업로더")
                .userType(UserType.KAHLUA)
                .loginType(LoginType.GENERAL)
                .build();
        userRepository.save(testUser);

        // 2. 테스트용 공유 앨범 1개 생성
        Album testAlbum = Album.builder()
                .title("2026년 깔루아 공식 공유 앨범")
                .build();
        albumRepository.save(testAlbum);

        // 3. 테스트용 사진 35장 생성 (공연 15장, 창립제 10장, 송년회 10장)
        for (int i = 1; i <= 35; i++) {
            Category category = Category.PERFORMANCE;
            if (i > 15 && i <= 25) category = Category.FOUNDATION_FESTIVAL;
            if (i > 25) category = Category.YEAR_END_PARTY;

            Photo photo = Photo.builder()
                    .album(testAlbum)
                    .uploader(testUser)
                    .thumbnailUrl("https://dummyimage.com/200x200/000/fff&text=Thumb_" + i)
                    .s3Key("kahlua/albums/dummy/origin/test.jpg")
                    .imageUrl("https://dummyimage.com/800x800/000/fff&text=Origin_" + i)
                    .category(category)
                    .build();

            photoRepository.save(photo);

            // 생성 시간 차이를 두기 위해 아주 짧게 대기 (정렬 테스트용)
            Thread.sleep(10);
        }

        System.out.println("✅ 더미 데이터 35장 생성 완료!");
    }
} */