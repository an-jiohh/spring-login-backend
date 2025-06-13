package jiohh.springlogin.user.dto;

import lombok.Getter;

@Getter
public class LogoutResponseDto {
    private String status;

    public LogoutResponseDto(String status) {
        this.status = status;
    }
}
