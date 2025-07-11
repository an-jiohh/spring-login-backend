package jiohh.springlogin.memo.handler;

import jiohh.springlogin.error.model.ErrorResponseDTO;
import jiohh.springlogin.memo.exception.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MemoExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("UNAUTHORIZED")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst().orElse("잘못된 요청입니다.");
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("BAD_REQUEST")
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
