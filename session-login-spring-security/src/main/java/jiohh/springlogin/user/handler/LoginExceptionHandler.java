package jiohh.springlogin.user.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jiohh.springlogin.error.model.ErrorResponseDTO;
import jiohh.springlogin.user.exception.DuplicateUserIdException;
import jiohh.springlogin.user.exception.InvalidCredentialsException;
import jiohh.springlogin.user.exception.SessionExpiredException;
import jiohh.springlogin.user.exception.SessionInvalidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "jiohh.springlogin.user")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoginExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(InvalidCredentialsException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status("error")
                .code("INVALID_CREDENTIALS")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(SessionInvalidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleSessionInvalidationException(SessionInvalidationException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status("error")
                .code("UNAUTHORIZED")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleSessionExpiredException(SessionExpiredException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status("error")
                .code("SESSION_EXPIRED")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DuplicateUserIdException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateUserIdException(DuplicateUserIdException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status("error")
                .code("VALIDATION_ERROR")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst().orElse("잘못된 요청입니다.");
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("BAD_REQUEST")
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
