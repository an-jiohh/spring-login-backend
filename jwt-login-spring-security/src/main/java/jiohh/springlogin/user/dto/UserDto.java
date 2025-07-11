package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    private Long id;
    private String userId;
    private String name;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
