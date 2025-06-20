package jiohh.springlogin.user.service;

import jiohh.springlogin.user.dto.*;
import jiohh.springlogin.user.exception.DuplicateUserIdException;
import jiohh.springlogin.user.model.RefreshToken;
import jiohh.springlogin.user.model.Role;
import jiohh.springlogin.user.model.User;
import jiohh.springlogin.user.repository.RefreshTokenRepository;
import jiohh.springlogin.user.repository.UserRepository;
import jiohh.springlogin.user.util.HashUtil;
import jiohh.springlogin.user.util.JwtUtil;
import jiohh.springlogin.user.util.SaltUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Optional<UserDto> login(LoginRequestDto loginRequestDto) {
        Optional<User> userOptional = userRepository.findByUserId(loginRequestDto.getUserId());
        if (userOptional.isEmpty()){
            return Optional.empty();
        }
        User user = userOptional.get();
        String storedPassword = user.getPassword();
        String salt = user.getSalt();
        String hash = HashUtil.sha256(salt + loginRequestDto.getPassword());
        if (!storedPassword.equals(salt + "$" + hash)) {
            return Optional.empty();
        }

        JwtPayloadDto tokenDto = JwtPayloadDto.builder()
                .sub(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .role(user.getRole())
                .build();

        String accessToken = jwtUtil.createAccessToken(tokenDto);
        String refreshToken = jwtUtil.createRefreshToken();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(user.getId())
                .expiresIn(System.currentTimeMillis() + jwtUtil.getRefreshTokenValiditySeconds())
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        UserDto response = UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return Optional.of(response);
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
