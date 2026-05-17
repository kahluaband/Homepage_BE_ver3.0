package kahlua.KahluaProject.domain.album.dto.request;

import lombok.Getter;
import java.util.List;

@Getter
public class PhotoDeleteRequest {
    private List<Long> photoIds;
}
