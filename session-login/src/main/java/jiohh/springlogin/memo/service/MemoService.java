package jiohh.springlogin.memo.service;

import jiohh.springlogin.memo.dto.MemoResponseDto;
import jiohh.springlogin.memo.exception.UserNotFoundException;
import jiohh.springlogin.memo.model.Memo;
import jiohh.springlogin.memo.repository.MemoRepository;
import jiohh.springlogin.user.model.User;
import jiohh.springlogin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    public List<MemoResponseDto> findAllByUserId(Long userId) {
        return memoRepository.findByUserId(userId)
                .stream().map(MemoResponseDto::from).collect(Collectors.toList());
    }

    @Transactional
    public MemoResponseDto create(Long userId, String content) {
        Optional<User> createUser = userRepository.findById(userId);
        if (createUser.isPresent()){
            User user = createUser.get();
            Memo memo = Memo.builder()
                    .content(content)
                    .user(user)
                    .build();
            Memo saved = memoRepository.save(memo);
            return MemoResponseDto.from(saved);
        } else {
            throw new UserNotFoundException();
        }
    }
}
