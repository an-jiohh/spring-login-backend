package jiohh.springlogin.memo.model;

import jakarta.persistence.*;
import jiohh.springlogin.user.model.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "memos")
@EntityListeners(AuditingEntityListener.class)
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 500, nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Memo() {
    }

    public Memo(long id, String content, LocalDateTime createAt, User user) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.user = user;
    }
}
