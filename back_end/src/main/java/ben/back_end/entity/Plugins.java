package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "plugins")
public class Plugins {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pluginId")
    private int pluginId;

    @Getter
    @Setter
    @Column(name = "pluginName", nullable = false, unique = true)
    private String pluginName;

    @Getter
    @Setter
    @Column(name = "pluginAvatar", nullable = false)
    private String pluginAvatar;

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
            joinColumns = @JoinColumn(name = "plugin_id", referencedColumnName = "pluginId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    )
    private Set<Tags> tags;
}
