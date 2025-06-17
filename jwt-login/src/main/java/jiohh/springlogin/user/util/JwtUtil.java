package jiohh.springlogin.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface JwtUtil {
    public String createAccessToken(String userId);
    public String createRefreshToken();
    public boolean validationToken(String token);
    public String getSubject(String token);
    public Map<String, Object> getClaims(String token);
}
