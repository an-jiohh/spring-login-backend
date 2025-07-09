package jiohh.springlogin.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jiohh.springlogin.response.ApiResponseDto;
import jiohh.springlogin.security.CustomUserDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletRequest request) {
        Optional<Authentication> loginResult = userService.login(loginRequestDto);
        if (loginResult.isPresent()) {
            Authentication authentication = loginResult.get();

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);


            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
;
            LoginResponseDto responseDto = LoginResponseDto.builder()
                    .userId(userDetails.getUserId())
                    .name(userDetails.getName())
                    .role(userDetails.getRole())
                    .build();
            return ResponseEntity.ok(ApiResponseDto.success(responseDto));
        } else {
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        log.info("Signup request: {}", signUpRequestDto);
        userService.registerUser(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.successWithNoData());
    }

//    @PostMapping("/logout")
//    public ResponseEntity<LogoutResponseDto> logout(HttpSession session){
//        try{
//            session.invalidate();
//        } catch (IllegalStateException e) {
//            throw new SessionInvalidationException();
//        }
//        return ResponseEntity.ok().body(new LogoutResponseDto("success"));
//    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<SessionCheckResponseDto>> checkSession(@AuthenticationPrincipal CustomUserDetails loginUser) {

        if (loginUser == null) {
            throw new SessionExpiredException();
        }

        SessionCheckResponseDto response = SessionCheckResponseDto.builder().userId(loginUser.getUserId()).name(loginUser.getName()).role(loginUser.getRole()).build();

        return ResponseEntity.ok().body(ApiResponseDto.success(response));
    }
}
