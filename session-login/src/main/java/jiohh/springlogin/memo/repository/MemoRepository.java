package jiohh.springlogin.memo.repository;

import jiohh.springlogin.memo.model.Memo;

import java.util.List;

public interface MemoRepository {
    public Memo save(Memo memo);
    public List<Memo> findByUserId(Long id);
}