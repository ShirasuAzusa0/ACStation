package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tag_relationships")
public class Tag_Relationships {
    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    private Tags tags;

    @Id
    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "videoId")
    private Videos videos;

    @Id
    @ManyToOne
    @JoinColumn(name = "skin_id", referencedColumnName = "skinId")
    private Skins skins;

    @Id
    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "carId")
    private Cars cars;

    @Id
    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "trackId")
    private Tracks tracks;

    @Id
    @ManyToOne
    @JoinColumn(name = "plugin_id", referencedColumnName = "pluginId")
    private Plugins plugins;
}
