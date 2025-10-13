package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgId")
    private int imgId;

    @ManyToOne
    @JoinColumn(name = "skin_id", referencedColumnName = "skinId")
    private Skins skins;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "carId")
    private Cars cars;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "trackId")
    private Tracks tracks;
}
