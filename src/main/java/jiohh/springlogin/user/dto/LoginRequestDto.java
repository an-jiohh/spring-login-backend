package jiohh.springlogin.user.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String password, String userid) {
        this.password = password;
        this.userId = userid;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
