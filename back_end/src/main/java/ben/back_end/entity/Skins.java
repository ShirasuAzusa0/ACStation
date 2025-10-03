package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "skins")
public class Skins {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skinId")
    private int skinId;

    @Getter
    @Setter
    @Column(name = "skinName", nullable = false, unique = true)
    private String skinName;

    @Getter
    @Setter
    @Column(name = "skinAvatar", nullable = false)
    private String skinAvatar;

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
            name = "tag_relationships",
            joinColumns = @JoinColumn(name = "skin_id", referencedColumnName = "skinId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    )
    private Set<Tags> tags;
}
