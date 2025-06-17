package jiohh.springlogin.user.util;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class JwtUtilImplTest {

    private JwtUtil jwtUtil;
    private String userId = "123";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtilImpl("test-secret-key", 60L, 60L);
    }

    @Test
    @DisplayName("AccessToken 생성")
    void testGenerateAccessToken() {
        String accessToken = jwtUtil.createAccessToken(userId);
        Assertions.assertThat(accessToken).isNotNull();
        log.info("access token: {}", accessToken);
    }

    @Test
    @DisplayName("RefreshToken 생성")
    void testGenerateRefreshToken() {
        String refreshToken = jwtUtil.createRefreshToken();
        Assertions.assertThat(refreshToken).isNotNull();
        log.info("refresh token: {}", refreshToken);
    }

    @Test
    @DisplayName("validate 성공")
    void testSuccessValidateToken() {
        String accessToken = jwtUtil.createAccessToken(userId);
        boolean check = jwtUtil.validationToken(accessToken);
        Assertions.assertThat(check).isTrue();
    }

    @Test
    @DisplayName("validate 실패 - 변조된 토큰")
    void testFailValidateToken() {
        String accessToken = jwtUtil.createAccessToken(userId);
        String[] splitToken = accessToken.split("\\.");
        String failToken = splitToken[0] + "." + splitToken[1] + "." + "invalid";
        boolean check = jwtUtil.validationToken(failToken);
        Assertions.assertThat(check).isFalse();
    }

    @Test
    @DisplayName("validate 실패 - 토큰 시간 초과")
    void testTimeLimitFailValidateToken() {
        JwtUtilImpl jwtTestUtil = new JwtUtilImpl("test-secret", -1000, -1);
        String accessToken = jwtTestUtil.createAccessToken(userId);
        boolean check = jwtTestUtil.validationToken(accessToken);
        Assertions.assertThat(check).isFalse();
    }


    @Test
    @DisplayName("Payload 정보 가져오기 - sub")
    void testGetSubject(){
        String accessToken = jwtUtil.createAccessToken(userId);
        String subject = jwtUtil.getSubject(accessToken);
        Assertions.assertThat(subject).isEqualTo(userId);

        String refreshToken = jwtUtil.createRefreshToken();
        subject = jwtUtil.getSubject(refreshToken);
        Assertions.assertThat(subject).isEqualTo(null);
    }

    @Test
    @DisplayName("Payload 정보 가져오기 - claims 전체")
    void testGetClaims(){
        String accessToken = jwtUtil.createAccessToken(userId);
        Map<String, Object> claims = jwtUtil.getClaims(accessToken);
        String user = (String) claims.get("sub");
        Assertions.assertThat(user).isEqualTo(userId);
    }

}