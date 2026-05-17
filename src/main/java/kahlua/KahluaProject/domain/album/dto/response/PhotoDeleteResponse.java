package kahlua.KahluaProject.domain.album.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class PhotoDeleteResponse {
    private List<Long> deletedPhotoIds;
    private Integer deletedCount;
}
