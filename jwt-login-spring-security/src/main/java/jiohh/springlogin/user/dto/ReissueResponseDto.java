package jiohh.springlogin.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReissueResponseDto {
    private String accessToken;

    public ReissueResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public ReissueResponseDto() {
    }
}
