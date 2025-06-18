package jiohh.springlogin.user.util;

import jiohh.springlogin.user.dto.JwtPayloadDto;
import jiohh.springlogin.user.dto.UserDto;
import jiohh.springlogin.user.model.Role;
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
    private JwtPayloadDto loginUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtilImpl("test-secret-key", 60L, 60L);
        loginUser = JwtPayloadDto.builder()
                .sub(100L)
                .role(Role.USER)
                .name("test-user")
                .userId("test-user-login-id")
                .build();
    }

    @Test
    @DisplayName("AccessToken 생성")
    void testGenerateAccessToken() {
        String accessToken = jwtUtil.createAccessToken(loginUser);
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
        String accessToken = jwtUtil.createAccessToken(loginUser);
        boolean check = jwtUtil.validationToken(accessToken);
        Assertions.assertThat(check).isTrue();
    }

    @Test
    @DisplayName("validate 실패 - 변조된 토큰")
    void testFailValidateToken() {
        String accessToken = jwtUtil.createAccessToken(loginUser);
        String[] splitToken = accessToken.split("\\.");
        String failToken = splitToken[0] + "." + splitToken[1] + "." + "invalid";
        boolean check = jwtUtil.validationToken(failToken);
        Assertions.assertThat(check).isFalse();
    }

    @Test
    @DisplayName("validate 실패 - 토큰 시간 초과")
    void testTimeLimitFailValidateToken() {
        JwtUtilImpl jwtTestUtil = new JwtUtilImpl("test-secret", -1000, -1);
        String accessToken = jwtTestUtil.createAccessToken(loginUser);
        boolean check = jwtTestUtil.validationToken(accessToken);
        Assertions.assertThat(check).isFalse();
    }


    @Test
    @DisplayName("Payload 정보 가져오기 - sub")
    void testGetSubject(){
        String accessToken = jwtUtil.createAccessToken(loginUser);
        Long subject = jwtUtil.getSubject(accessToken);

        Assertions.assertThat(subject).isEqualTo(loginUser.getSub());

        String refreshToken = jwtUtil.createRefreshToken();
        subject = jwtUtil.getSubject(refreshToken);
        Assertions.assertThat(subject).isEqualTo(null);
    }

    @Test
    @DisplayName("Payload 정보 가져오기 - claims 전체")
    void testGetClaims(){
        String accessToken = jwtUtil.createAccessToken(loginUser);
        Map<String, Object> claims = jwtUtil.getClaims(accessToken);
        Number user = (Number) claims.get("sub");
        Assertions.assertThat(user.longValue()).isEqualTo(loginUser.getSub());
    }

    @Test
    @DisplayName("Payload 정보 가져오기 - JwtPayloadDto 객체")
    void testGetJwtPayloadDto(){
        String accessToken = jwtUtil.createAccessToken(loginUser);
        JwtPayloadDto checkUser = jwtUtil.getUser(accessToken);
//        iat, exp 값 때문에 id 값으로 비교
        Assertions.assertThat(checkUser.getUserId()).isEqualTo(loginUser.getUserId());
    }

}