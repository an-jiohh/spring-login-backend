package jiohh.springlogin.user.controller;

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

//    TODO : session -> request.getSession(true);로 변경
    @PostMapping("login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                        HttpSession session) {
        Optional<LoginResponseDto> loginResult = userService.login(loginRequestDto);
        if (loginResult.isPresent()) {
            LoginResponseDto dto = loginResult.get();
            session.setAttribute("userId", dto.getUserId());
            session.setAttribute("role", dto.getRole());
            session.setAttribute("name", dto.getName());
            session.setMaxInactiveInterval(60 * 30);
            LoginResponseDto response = LoginResponseDto.builder().userId(dto.getUserId()).name(dto.getName()).role(dto.getRole()).build();
            return ResponseEntity.ok(ApiResponseDto.success(response));
        } else {
            throw new InvalidCredentialsException();
        }
    }

//    TODO : @SessionAttribute로 개선
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
        String userId = (String) session.getAttribute("userId");
        String name = (String) session.getAttribute("name");
        Role role = (Role) session.getAttribute("role");

        if (userId == null || name == null || role == null) {
            throw new SessionExpiredException();
        }

        SessionCheckResponseDto response = SessionCheckResponseDto.builder().userId(userId).name(name).role(role).build();

        return ResponseEntity.ok().body(ApiResponseDto.success(response));
    }
}
