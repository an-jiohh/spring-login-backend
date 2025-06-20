package jiohh.springlogin.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jiohh.springlogin.response.ApiResponseDto;
import jiohh.springlogin.user.dto.*;
import jiohh.springlogin.user.exception.InvalidCredentialsException;
import jiohh.springlogin.user.exception.InvalidRefreshTokenException;
import jiohh.springlogin.user.exception.SessionExpiredException;
import jiohh.springlogin.user.exception.SessionInvalidationException;
import jiohh.springlogin.user.service.UserService;
import jiohh.springlogin.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Optional<UserDto> loginResult = userService.login(loginRequestDto);
        if (loginResult.isPresent()) {
            UserDto userDto = loginResult.get();


            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", userDto.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge((int) (jwtUtil.getRefreshTokenValiditySeconds() / 1000))
                    .build();
            response.addHeader("Set-Cookie",refreshTokenCookie.toString());

            LoginResponseDto responseDto = LoginResponseDto.builder()
                    .id(userDto.getId())
                    .userId(userDto.getUserId())
                    .role(userDto.getRole())
                    .name(userDto.getName())
                    .accessToken(userDto.getAccessToken())
                    .build();
            return ResponseEntity.ok(ApiResponseDto.success(responseDto));
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseDto<ReissueResponseDto>> reissueAccessToken(@CookieValue("refreshToken") String refreshToken) {
        if (refreshToken == null || !jwtUtil.validationToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }
        String reissueAccessToken = userService.reissueAccessToken(refreshToken);

        ReissueResponseDto response = ReissueResponseDto.builder()
                .accessToken(reissueAccessToken)
                .build();

        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto,
                                                                HttpSession session) {
        log.info("Signup request: {}", signUpRequestDto);
        userService.registerUser(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.successWithNoData());
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null){
            throw new InvalidRefreshTokenException();
        }
        userService.logout(refreshToken);

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie",responseCookie.toString());

        return ResponseEntity.ok().body(new LogoutResponseDto("success"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<SessionCheckResponseDto>> checkSession(HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new SessionExpiredException();
        }

        SessionCheckResponseDto response = SessionCheckResponseDto.builder().userId(loginUser.getUserId()).name(loginUser.getName()).role(loginUser.getRole()).build();

        return ResponseEntity.ok().body(ApiResponseDto.success(response));
    }
}
