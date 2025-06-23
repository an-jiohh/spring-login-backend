package jiohh.springlogin.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jiohh.springlogin.user.model.RefreshToken;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void save(RefreshToken refreshToken) {
        em.persist(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        String sql = "SELECT rt FROM RefreshToken rt WHERE rt.userId = :userId";
        TypedQuery<RefreshToken> query = em.createQuery(sql, RefreshToken.class);
        query.setParameter("userId", userId);
        try{
            RefreshToken singleResult = query.getSingleResult();
            return Optional.of(singleResult);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT rt FROM RefreshToken rt WHERE rt.refreshToken = :token";
        TypedQuery<RefreshToken> query = em.createQuery(sql, RefreshToken.class);
        query.setParameter("token", token);
        try{
            RefreshToken singleResult = query.getSingleResult();
            return Optional.of(singleResult);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByToken(RefreshToken refreshToken) {
        if (em.contains(refreshToken)) {
            em.remove(refreshToken);
        } else {
            em.remove(em.merge(refreshToken));
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        String sql = "DELETE FROM RefreshToken rt WHERE rt.userId = :userId";
        Query query = em.createQuery(sql);
        query.setParameter("userId", userId);
        int deleteCount = query.executeUpdate();
    }
}
