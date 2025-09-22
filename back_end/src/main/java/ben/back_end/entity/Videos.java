package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "videos")
public class Videos {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videoId")
    private int videoId;

    @Getter
    @Setter
    @Column(name = "videoTitle", nullable = false, unique = true)
    private String videoTitle;

    @Getter
    @Setter
    @Column(name = "videoAvatar", nullable = false)
    private String videoAvatar;

    @Getter
    @Setter
    @Column(name = "views", nullable = false)
    private int views;

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
            joinColumns = @JoinColumn(name = "video_id", referencedColumnName = "videoId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    )
    private Set<Tags> tags;
}
