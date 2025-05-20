package jiohh.springlogin.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jiohh.springlogin.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findByUserId(String userId) {
        String sql = "SELECT u FROM User u WHERE u.userId = :userId";
        TypedQuery<User> query = em.createQuery(sql, User.class);
        query.setParameter("userId", userId);
        try{
            return Optional.of(query.getSingleResult());
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public void delete(User user) {

    }
}
