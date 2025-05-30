package jiohh.springlogin.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class SignUpRequestDto {

    private String userId;
    private String password;
    private String name;

    public SignUpRequestDto() {
    }

    public SignUpRequestDto(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
