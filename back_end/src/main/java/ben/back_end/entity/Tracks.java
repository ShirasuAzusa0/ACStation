package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tracks")
public class Tracks {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trackId")
    private int trackId;

    @Getter
    @Setter
    @Column(name = "trackModName", nullable = false, unique = true)
    private String trackModName;

    @Getter
    @Setter
    @Column(name = "trackAvatar", nullable = false)
    private String trackAvatar;

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
            joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "trackId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    )
    private Set<Tags> tags;
}
