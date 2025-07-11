package jiohh.springlogin.user.service;

import jiohh.springlogin.security.CustomUserDetails;
import jiohh.springlogin.user.dto.*;
import jiohh.springlogin.user.exception.DuplicateUserIdException;
import jiohh.springlogin.user.exception.InvalidRefreshTokenException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Optional<UserDto> login(LoginRequestDto loginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(), loginRequestDto.getPassword())
        );

        CustomUserDetails user = (CustomUserDetails) authenticate.getPrincipal();

        JwtPayloadDto tokenDto = JwtPayloadDto.builder()
                .sub(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .role(user.getRole())
                .build();

        String accessToken = jwtUtil.createAccessToken(tokenDto);
        String refreshToken = jwtUtil.createRefreshToken();

        saveRefreshToken(user.getId(), refreshToken);

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

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        Optional<RefreshToken> boxedRefreshToken = refreshTokenRepository.findByUserId(userId);
        RefreshToken refreshTokenObject;
        if (boxedRefreshToken.isPresent()) {
            refreshTokenObject = boxedRefreshToken.get();
            refreshTokenObject.updateRefreshToken(newRefreshToken);
        } else {
            refreshTokenObject = RefreshToken.builder()
                    .refreshToken(newRefreshToken)
                    .userId(userId)
                    .expiresIn(System.currentTimeMillis() + jwtUtil.getRefreshTokenValiditySeconds())
                    .build();
        }
        refreshTokenRepository.save(refreshTokenObject);
    }

    @Transactional
    public void registerUser(SignUpRequestDto signUpRequestDto) {
        if(userRepository.findByUserId(signUpRequestDto.getUserId()).isPresent()) {
            throw new DuplicateUserIdException("이미 존재하는 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User user = User.builder()
                        .userId(signUpRequestDto.getUserId())
                .password(encodedPassword)
                .name(signUpRequestDto.getName())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @Transactional
    public String reissueAccessToken(String refreshToken) {
        Optional<RefreshToken> boxedSavedToken = refreshTokenRepository.findByToken(refreshToken);
        if (boxedSavedToken.isEmpty()){
            throw new InvalidRefreshTokenException();
        }
        RefreshToken savedToken = boxedSavedToken.get();
        if (isTokenExpired(savedToken)) {
            throw new InvalidRefreshTokenException();
        }

        Optional<User> byId = userRepository.findById(savedToken.getUserId());
        if (byId.isEmpty()){
            throw new InvalidRefreshTokenException("토큰의 유저정보를 찾을 수 없습니다.");
        }
        User user = byId.get();
        JwtPayloadDto dto = JwtPayloadDto.builder()
                .sub(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .role(user.getRole())
                .build();
        String accessToken = jwtUtil.createAccessToken(dto);
        return accessToken;
    }

    private Boolean isTokenExpired(RefreshToken token) {
        return token.getExpiresIn() < System.currentTimeMillis();
    }

    @Transactional
    public void logout(String refreshToken) {
        Optional<RefreshToken> boxedSavedToken = refreshTokenRepository.findByToken(refreshToken);
        if (boxedSavedToken.isPresent()){
            RefreshToken savedToken = boxedSavedToken.get();
            refreshTokenRepository.deleteByToken(savedToken);
        }
    }
}
