package jiohh.springlogin.user.model;

import jakarta.persistence.*;
import jiohh.springlogin.memo.model.Memo;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String userId;
    @Column(nullable = false, length = 50)
    private String password;
    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

//    단방향으로 사용하기 위해 제거
//    TODO JPQL으로 MEMO 조회
//    @OneToMany(mappedBy = "user")
//    private List<Memo> memos = new ArrayList<>();

    public User() {
    }

    public User(Long id, String userId, String password, String name, Role role) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
