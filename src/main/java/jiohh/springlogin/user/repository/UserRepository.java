package jiohh.springlogin.user.repository;


import jiohh.springlogin.user.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);
    Optional<User> findByUserId(String userId);
    void save(User user);
    void update(User user);
    void delete(User user);
}
