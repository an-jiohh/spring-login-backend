package jiohh.springlogin.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 50, message = "아이디는 4자 이상이어야 합니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 50, message = "비밀번호는 8자 이상이어야 합니다.")
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
