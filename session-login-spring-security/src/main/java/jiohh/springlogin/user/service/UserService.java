package jiohh.springlogin.user.service;

import jiohh.springlogin.user.dto.LoginRequestDto;
import jiohh.springlogin.user.dto.LoginResponseDto;
import jiohh.springlogin.user.dto.LoginSessionDto;
import jiohh.springlogin.user.dto.SignUpRequestDto;
import jiohh.springlogin.user.exception.DuplicateUserIdException;
import jiohh.springlogin.user.model.Role;
import jiohh.springlogin.user.model.User;
import jiohh.springlogin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public Optional<Authentication> login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(), loginRequestDto.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authToken);
            return Optional.of(authenticate);
        } catch (UsernameNotFoundException | BadCredentialsException e){
            return Optional.empty();
        }
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
}
