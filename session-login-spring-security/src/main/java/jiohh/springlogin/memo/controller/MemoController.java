package jiohh.springlogin.memo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jiohh.springlogin.memo.dto.MemoCreateRequestDto;
import jiohh.springlogin.memo.dto.MemoResponseDto;
import jiohh.springlogin.memo.model.Memo;
import jiohh.springlogin.memo.service.MemoService;
import jiohh.springlogin.response.ApiResponseDto;
import jiohh.springlogin.security.CustomUserDetails;
import jiohh.springlogin.user.dto.LoginSessionDto;
import jiohh.springlogin.user.exception.SessionExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/memos")
    public ResponseEntity<ApiResponseDto<List<MemoResponseDto>>> findMemos(@AuthenticationPrincipal CustomUserDetails loginUser) {
        List<MemoResponseDto> response = memoService.findAllByUserId(loginUser.getId());
        return ResponseEntity.ok().body(new ApiResponseDto<>(response));
    }

    @PostMapping("/memos")
    public ResponseEntity<ApiResponseDto<MemoResponseDto>> createMemo(@RequestBody @Valid MemoCreateRequestDto memoCreate, @AuthenticationPrincipal CustomUserDetails loginUser ) {
        MemoResponseDto saved_memo = memoService.create(loginUser.getId(), memoCreate.getContent());
        return ResponseEntity.ok().body(ApiResponseDto.success(saved_memo));
    }
}
