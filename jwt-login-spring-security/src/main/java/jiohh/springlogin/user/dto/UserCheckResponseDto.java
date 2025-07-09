package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCheckResponseDto {
    private Long id;
    private String userId;
    private String name;
    private Role role;

    public UserCheckResponseDto() {
    }

    public UserCheckResponseDto(Long id, String userId, String name, Role role) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}
