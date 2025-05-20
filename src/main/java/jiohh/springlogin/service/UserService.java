package jiohh.springlogin.service;

import jiohh.springlogin.dto.LoginRequestDto;
import jiohh.springlogin.dto.LoginResponseDto;
import jiohh.springlogin.dto.SignUpRequestDto;
import jiohh.springlogin.model.Role;
import jiohh.springlogin.model.User;
import jiohh.springlogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Optional<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        Optional<User> userOptional = userRepository.findByUserId(loginRequestDto.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("User logged in: {}", user);
            if (user.getPassword().equals(loginRequestDto.getPassword())) {
                LoginResponseDto dto = new LoginResponseDto(user.getUserId(), user.getName(), user.getRole());
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public void registerUser(SignUpRequestDto signUpRequestDto) {
        if(userRepository.findByUserId(signUpRequestDto.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        User user = User.builder()
                        .userId(signUpRequestDto.getUserId())
                .password((signUpRequestDto.getPassword()))
                .name(signUpRequestDto.getName())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }
}
