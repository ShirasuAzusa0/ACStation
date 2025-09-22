package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Cars {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carId")
    private int carId;

    @Getter
    @Setter
    @Column(name = "carModName", nullable = false, unique = true)
    private String carName;

    @Getter
    @Setter
    @Column(name = "carAvatar", nullable = false)
    private String carAvatar;

    @Getter
    @Setter
    @Column(name = "views", nullable = false)
    private int views;

    @Getter
    @Setter
    @Column(name = "likes", nullable = false)
    private int likes;

    @Getter
    @Setter
    @Column(name = "downloads", nullable = false)
    private int downloads;

    @Getter
    @Setter
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Column(name = "linkURL", nullable = false)
    private String linkURL;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "tag-relationships",
            joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "carId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    )
    private Set<Tags> tags;
}
