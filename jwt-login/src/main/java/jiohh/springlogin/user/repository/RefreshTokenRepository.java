package jiohh.springlogin.user.repository;

import jiohh.springlogin.user.model.RefreshToken;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByUserId(String userId);
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(RefreshToken refreshToken);
    void deleteByUserId(String userId);
//    int deleteAllExpiredBefore(Instant now); 차후 스케줄러 구현시 사용
}