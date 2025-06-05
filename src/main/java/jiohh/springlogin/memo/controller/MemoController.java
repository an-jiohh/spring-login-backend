package jiohh.springlogin.memo.controller;

import jakarta.servlet.http.HttpSession;
import jiohh.springlogin.memo.dto.MemoCreateRequestDto;
import jiohh.springlogin.memo.dto.MemoResponseDto;
import jiohh.springlogin.memo.model.Memo;
import jiohh.springlogin.memo.service.MemoService;
import jiohh.springlogin.response.ApiResponseDto;
import jiohh.springlogin.user.dto.LoginSessionDto;
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
    public ResponseEntity<ApiResponseDto<List<MemoResponseDto>>> findMemos(HttpSession session) {
        LoginSessionDto loginUser = (LoginSessionDto) session.getAttribute("loginUser");
        List<MemoResponseDto> response = memoService.findAllByUserId(loginUser.getId());
        return ResponseEntity.ok().body(new ApiResponseDto<>(response));
    }

    @PostMapping("/memos")
    public ResponseEntity<ApiResponseDto<MemoResponseDto>> createMemo(@RequestBody MemoCreateRequestDto memoCreate, HttpSession session) {
        LoginSessionDto loginUser = (LoginSessionDto) session.getAttribute("loginUser");
        MemoResponseDto saved_memo = memoService.create(loginUser.getId(), memoCreate.getContent());
        return ResponseEntity.ok().body(ApiResponseDto.success(saved_memo));
    }
}
