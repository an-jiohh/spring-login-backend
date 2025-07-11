package jiohh.springlogin.security;

import jiohh.springlogin.user.model.Role;
import jiohh.springlogin.user.model.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String userId;
    private String password;
    private String name;
    private Role role;

    public CustomUserDetails(Long id, String userId, String password, String name, Role role) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public CustomUserDetails() {
    }

    public CustomUserDetails(User user){
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }
}