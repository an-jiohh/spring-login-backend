package jiohh.springlogin.memo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoCreateRequestDto {

    @NotBlank(message = "메시지를 입력해주세요")
    @Size(max = 500, message = "내용은 최대 500자까지 입력해주세요.")
    private String content;

    public MemoCreateRequestDto(String content) {
        this.content = content;
    }
}
