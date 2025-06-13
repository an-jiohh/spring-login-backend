package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionCheckResponseDto {
    private String userId;
    private String name;
    private Role role;

    public SessionCheckResponseDto() {
    }

    public SessionCheckResponseDto(String userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}
