package jiohh.springlogin.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 50, message = "아이디는 4자 이상이어야 합니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 50, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 10, message = "이름 10자 이하이어야 합니다.")
    private String name;

    public SignUpRequestDto() {
    }

    public SignUpRequestDto(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }
}
