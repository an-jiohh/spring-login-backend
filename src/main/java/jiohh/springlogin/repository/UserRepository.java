package jiohh.springlogin.repository;


import jiohh.springlogin.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserId(String userId);
    void save(User user);
    void update(User user);
    void delete(User user);
}
