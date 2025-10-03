package ben.back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tags")
public class Tags {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId")
    private int tagId;

    @Getter
    @Setter
    @Column(name = "tagName")
    private String tagName;

    @Getter
    @Setter
    @Column(name = "category")
    private String category;
}
