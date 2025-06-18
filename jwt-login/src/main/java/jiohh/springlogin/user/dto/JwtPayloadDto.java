package jiohh.springlogin.user.dto;

import jiohh.springlogin.user.model.Role;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
public class JwtPayloadDto {
    private Long sub;
    private String userId;
    private String name;
    private Role role;
    private Long exp;
    private Long iat;
    public JwtPayloadDto() {
    }

    public JwtPayloadDto(Long sub, String userId, String name, Role role, Long exp, Long iat) {
        this.sub = sub;
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.exp = exp;
        this.iat = iat;
    }


}
