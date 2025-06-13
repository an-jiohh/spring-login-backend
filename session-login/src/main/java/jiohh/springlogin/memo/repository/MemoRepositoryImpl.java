package jiohh.springlogin.memo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jiohh.springlogin.memo.model.Memo;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.List;

@Repository
public class MemoRepositoryImpl implements MemoRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Memo save(Memo memo) {
        em.persist(memo);
        return memo;
    }

    @Override
    public List<Memo> findByUserId(Long id) {
        String jpql = "SELECT m FROM Memo m where m.user.id = :id";
        TypedQuery<Memo> query = em.createQuery(jpql, Memo.class);
        query.setParameter("id", id);
        List<Memo> result = query.getResultList();
        return result;
    }
}
