package jiohh.springlogin.user.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jiohh.springlogin.error.model.ErrorResponseDTO;
import jiohh.springlogin.user.exception.DuplicateUserIdException;
import jiohh.springlogin.user.exception.InvalidCredentialsException;
import jiohh.springlogin.user.exception.SessionExpiredException;
import jiohh.springlogin.user.exception.SessionInvalidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "jiohh.springlogin.user")
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
}
