package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private Long id;
    private String userId;
    private String name;
    private Role role;
    private String accessToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(Long id, String userId, String name, Role role, String accessToken) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.accessToken = accessToken;
    }
}
