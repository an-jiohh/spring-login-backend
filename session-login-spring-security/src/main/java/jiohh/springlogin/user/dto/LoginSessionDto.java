package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginSessionDto {
    private Long id;
    private String userId;
    private String name;
    private Role role;
}
