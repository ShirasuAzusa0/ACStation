package ben.back_end.repository;

import ben.back_end.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tags, Long> {
    @Query(value = """
    SELECT t.tagId, t.tagName, t.video_id, t.skin_id, t.car_id, t.track_id, t.plugin_id
    FROM tags t
    INNER JOIN (
        SELECT MIN(tagId) AS id FROM tags GROUP BY tagName
    ) t2 ON t.tagId = t2.id
""", nativeQuery = true)
    List<Tags> findAll();
}
