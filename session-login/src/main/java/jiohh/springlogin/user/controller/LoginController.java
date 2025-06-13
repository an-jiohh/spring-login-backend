package jiohh.springlogin.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jiohh.springlogin.response.ApiResponseDto;
import jiohh.springlogin.user.dto.*;
import jiohh.springlogin.user.exception.InvalidCredentialsException;
import jiohh.springlogin.user.exception.SessionExpiredException;
import jiohh.springlogin.user.exception.SessionInvalidationException;
import jiohh.springlogin.user.model.Role;
import jiohh.springlogin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletRequest request) {
        Optional<LoginSessionDto> loginResult = userService.login(loginRequestDto);
        if (loginResult.isPresent()) {
            LoginSessionDto loginUser = loginResult.get();
            request.getSession().setAttribute("loginUser", loginUser);
            LoginResponseDto responseDto = LoginResponseDto.builder()
                    .userId(loginUser.getUserId())
                    .name(loginUser.getName())
                    .role(loginUser.getRole())
                    .build();
            return ResponseEntity.ok(ApiResponseDto.success(responseDto));
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto,
                                                                HttpSession session) {
        log.info("Signup request: {}", signUpRequestDto);
        userService.registerUser(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.successWithNoData());
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpSession session){
        try{
            session.invalidate();
        } catch (IllegalStateException e) {
            throw new SessionInvalidationException();
        }
        return ResponseEntity.ok().body(new LogoutResponseDto("success"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<SessionCheckResponseDto>> checkSession(HttpSession session) {
        LoginSessionDto loginUser = (LoginSessionDto) session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new SessionExpiredException();
        }

        SessionCheckResponseDto response = SessionCheckResponseDto.builder().userId(loginUser.getUserId()).name(loginUser.getName()).role(loginUser.getRole()).build();

        return ResponseEntity.ok().body(ApiResponseDto.success(response));
    }
}
