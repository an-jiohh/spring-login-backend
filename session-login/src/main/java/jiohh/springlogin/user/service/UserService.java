package jiohh.springlogin.user.service;

import jiohh.springlogin.user.dto.LoginRequestDto;
import jiohh.springlogin.user.dto.LoginResponseDto;
import jiohh.springlogin.user.dto.LoginSessionDto;
import jiohh.springlogin.user.dto.SignUpRequestDto;
import jiohh.springlogin.user.exception.DuplicateUserIdException;
import jiohh.springlogin.user.model.Role;
import jiohh.springlogin.user.model.User;
import jiohh.springlogin.user.repository.UserRepository;
import jiohh.springlogin.user.util.HashUtil;
import jiohh.springlogin.user.util.SaltUtil;
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

    public Optional<LoginSessionDto> login(LoginRequestDto loginRequestDto) {
        Optional<User> userOptional = userRepository.findByUserId(loginRequestDto.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String[] parts = user.getPassword().split("\\$");
            String salt = parts[0];
            String storedHash = parts[1];
            String hash = HashUtil.sha256(salt + loginRequestDto.getPassword());
            if (storedHash.equals(hash)) {
                LoginSessionDto dto = LoginSessionDto.builder()
                        .id(user.getId())
                        .userId(user.getUserId())
                        .name(user.getName())
                        .role(user.getRole())
                        .build();
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public void registerUser(SignUpRequestDto signUpRequestDto) {
        if(userRepository.findByUserId(signUpRequestDto.getUserId()).isPresent()) {
            throw new DuplicateUserIdException("이미 존재하는 아이디입니다.");
        }
        String salt = SaltUtil.generateSalt();
        String password = signUpRequestDto.getPassword();
        String hash = HashUtil.sha256(salt + password);
        String encodedPassword = salt + "$" + hash;

        User user = User.builder()
                        .userId(signUpRequestDto.getUserId())
                .password(encodedPassword)
                .name(signUpRequestDto.getName())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }
}
