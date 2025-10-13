package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Getter
    @Setter
    @Column(name = "type", nullable = false)
    private String type;

    @Getter
    @Setter
    @Column(name = "cat_id", nullable = false)
    private int cat_id;
}
