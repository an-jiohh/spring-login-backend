package jiohh.springlogin.user.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jiohh.springlogin.security.CustomUserDetails;
import jiohh.springlogin.user.dto.JwtPayloadDto;
import jiohh.springlogin.user.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtUtilImpl implements JwtUtil {

//    예시
//    private final long accessTokenValiditySeconds = 1000 * 60 * 30;
//    private final long refreshTokenValiditySeconds = 1000 * 60 * 60 * 24 * 7;
    private final SecretKey secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtParser jwtParser;

    public Long getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public Long getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public JwtUtilImpl(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.accessTokenSeconds}") long accessTokenValiditySeconds,
                       @Value("${jwt.refreshTokenSeconds}") long refreshTokenValiditySeconds ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public String createAccessToken(JwtPayloadDto user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenValiditySeconds);

        return Jwts.builder()
                .claim("id", user.getSub())
                .claim("userId", user.getUserId())
                .claim("name", user.getName())
                .claim("role",user.getRole())
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String createRefreshToken() {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshTokenValiditySeconds);
        return Jwts.builder()
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean validationToken(String token) {
        try{
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | SignatureException e ) {
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Long getSubject(String token) {
        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        Claims payload = claimsJws.getPayload();
        return payload.get("id", Long.class);
    }

    @Override
    public Map<String, Object> getClaims(String token)  {
        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        Claims payload = claimsJws.getPayload();
        return payload;
    }

    @Override
    public CustomUserDetails getUser(String token) {
        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        Claims payload = claimsJws.getPayload();
        String strRole = payload.get("role", String.class);
        Role role = Role.valueOf(strRole);
        return CustomUserDetails.builder()
                .id(payload.get("id", Long.class))
                .userId(payload.get("userId", String.class))
                .name(payload.get("name", String.class))
                .role(role)
                .build();
    }
}
