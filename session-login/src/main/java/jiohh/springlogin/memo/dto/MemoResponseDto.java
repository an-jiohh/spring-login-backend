package jiohh.springlogin.memo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jiohh.springlogin.memo.model.Memo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemoResponseDto {
    private Long id;
    private String content;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime createAt;

    public MemoResponseDto() {
    }

    public MemoResponseDto(Long id, String content, LocalDateTime createAt) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
    }

    public static MemoResponseDto from(Memo memo){
        MemoResponseDto dto = new MemoResponseDto();
        dto.id = memo.getId();
        dto.content = memo.getContent();
        dto.createAt = memo.getCreateAt();
        return dto;
    }
}
