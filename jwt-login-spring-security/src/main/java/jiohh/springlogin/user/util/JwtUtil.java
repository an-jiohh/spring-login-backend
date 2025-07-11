package jiohh.springlogin.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import jiohh.springlogin.security.CustomUserDetails;
import jiohh.springlogin.user.dto.JwtPayloadDto;
import jiohh.springlogin.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface JwtUtil {
    public String createAccessToken(JwtPayloadDto userDto);
    public String createRefreshToken();
    public boolean validationToken(String token);
    public Long getSubject(String token);
    public Map<String, Object> getClaims(String token);
    public CustomUserDetails getUser(String token);
    public Long getRefreshTokenValiditySeconds();
    public Long getAccessTokenValiditySeconds();
}
