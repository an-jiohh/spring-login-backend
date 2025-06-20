package jiohh.springlogin.memo.controller;

import jakarta.validation.Valid;
import jiohh.springlogin.memo.dto.MemoCreateRequestDto;
import jiohh.springlogin.memo.dto.MemoResponseDto;
import jiohh.springlogin.memo.service.MemoService;
import jiohh.springlogin.resolver.LoginUser;
import jiohh.springlogin.response.ApiResponseDto;
import jiohh.springlogin.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/memos")
    public ResponseEntity<ApiResponseDto<List<MemoResponseDto>>> findMemos(@LoginUser UserDto loginUser) {
        List<MemoResponseDto> response = memoService.findAllByUserId(loginUser.getId());
        return ResponseEntity.ok().body(new ApiResponseDto<>(response));
    }

    @PostMapping("/memos")
    public ResponseEntity<ApiResponseDto<MemoResponseDto>> createMemo(@RequestBody @Valid MemoCreateRequestDto memoCreate, @LoginUser UserDto loginUser) {
        MemoResponseDto saved_memo = memoService.create(loginUser.getId(), memoCreate.getContent());
        return ResponseEntity.ok().body(ApiResponseDto.success(saved_memo));
    }
}
