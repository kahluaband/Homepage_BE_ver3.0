package kahlua.KahluaProject.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostImageCreateRequest {

    @Schema(description = "image url")
    private String url;
}
