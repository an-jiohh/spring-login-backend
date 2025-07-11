package jiohh.springlogin.error.handler;

import jiohh.springlogin.error.model.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("DATA_INTEGRITY_ERROR")
                .message("서버 오류가 발생하였습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> runtimeExceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .code("INTERNAL_ERROR")
                .message("서버 오류가 발생하였습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
