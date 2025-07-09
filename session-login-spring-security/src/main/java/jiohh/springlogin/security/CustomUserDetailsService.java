package jiohh.springlogin.security;

import jiohh.springlogin.user.model.User;
import jiohh.springlogin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUserId = userRepository.findByUserId(username);
        if (byUserId.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        User user = byUserId.get();
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .password(user.getPassword())
                .id(user.getId())
                .name(user.getName())
                .build();
        return userDetails;
    }
}
