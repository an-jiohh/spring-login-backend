package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;

public class LoginResponseDto {
    private String userId;
    private String name;
    private Role role;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
