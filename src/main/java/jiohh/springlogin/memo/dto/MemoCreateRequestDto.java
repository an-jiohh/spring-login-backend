package jiohh.springlogin.memo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoCreateRequestDto {
    private String content;

    public MemoCreateRequestDto(String content) {
        this.content = content;
    }
}
