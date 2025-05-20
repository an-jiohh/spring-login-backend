package jiohh.springlogin.controller;

import jakarta.servlet.http.HttpSession;
import jiohh.springlogin.dto.LoginRequestDto;
import jiohh.springlogin.dto.LoginResponseDto;
import jiohh.springlogin.dto.SignUpRequestDto;
import jiohh.springlogin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto,
                        HttpSession session) {
        Optional<LoginResponseDto> loginResult = userService.login(loginRequestDto);
        if (loginResult.isPresent()) {
            LoginResponseDto dto = loginResult.get();
            session.setAttribute("userId", dto.getUserId());
            session.setAttribute("role", dto.getRole());
            session.setAttribute("name", dto.getName());
            session.setMaxInactiveInterval(60 * 30);
            return ResponseEntity.ok(new LoginResponseDto(dto.getUserId(), dto.getName(), dto.getRole()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody SignUpRequestDto signUpRequestDto,
                               HttpSession session) {
        log.info("Signup request: {}", signUpRequestDto);
        try{
            userService.registerUser(signUpRequestDto);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            return "signup";
        }
    }

    @GetMapping("/session-check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return ResponseEntity.ok("현재 세션 userId: " + userId);
    }
}
