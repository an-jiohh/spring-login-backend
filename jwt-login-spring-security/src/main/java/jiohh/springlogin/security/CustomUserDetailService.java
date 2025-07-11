package jiohh.springlogin.security;

import jiohh.springlogin.user.exception.InvalidCredentialsException;
import jiohh.springlogin.user.model.User;
import jiohh.springlogin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserId(username);
        if (optionalUser.isEmpty()){
            throw new InvalidCredentialsException();
        }
        User loginUser = optionalUser.get();
        return new CustomUserDetails(loginUser);
    }
}
