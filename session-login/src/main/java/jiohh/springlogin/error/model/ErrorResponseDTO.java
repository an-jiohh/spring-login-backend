package jiohh.springlogin.error.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ErrorResponseDTO {
    private String status = "error";
    private String code;
    private String message;

    public ErrorResponseDTO(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
