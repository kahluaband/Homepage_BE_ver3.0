package kahlua.KahluaProject.domain.album.facade;

import kahlua.KahluaProject.domain.album.dto.request.ReactionRequest;
import kahlua.KahluaProject.domain.album.dto.response.ReactionResponse;
import kahlua.KahluaProject.domain.album.service.AlbumService;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ReactionLockFacade {

    private final RedissonClient redissonClient;
    private final AlbumService albumService;

    public ReactionResponse toggleReactionWithLock(Long albumId, Long photoId, ReactionRequest request, User currentUser) {

        String lockKey = String.format("lock:reaction:photo:%d:user:%d", photoId, currentUser.getId());
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 대기 시간(3초), 락 점유 시간(5초)
            boolean available = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (!available) {
                // 너무 많은 동시 요청
                throw new GeneralException(ErrorStatus.TOO_MANY_REQUESTS);
            }

            return albumService.toggleReaction(albumId, photoId, request, currentUser);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("이모지 반응 처리 중 락 획득에 실패했습니다.", e);
        } finally {
            // 락 해제
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}