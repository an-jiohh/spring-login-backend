package jiohh.springlogin.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jiohh.springlogin.user.model.User;
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
    public Optional<User> findById(long id){
        String jpql = "SELECT u From User u WHERE u.id = :id";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("id", id);
        try{
            return Optional.of(query.getSingleResult());
        } catch (Exception e){
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
