package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Getter
    @Setter
    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Getter
    @Setter
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Getter
    @Setter
    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Getter
    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Getter
    @Setter
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Column(name = "lastConnectedAt", nullable = false)
    private LocalDateTime lastConnectedAt;

    @Getter
    @Setter
    @Column(name = "type", nullable = false, columnDefinition = "enum('user', 'admin')")
    @Enumerated(EnumType.STRING)
    private userType type;

}
