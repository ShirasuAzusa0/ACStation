package ben.back_end.repository;

import ben.back_end.entity.Images;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Images, Integer> {
    @Query(value = """
        SELECT imgPath
        FROM images
        WHERE skin_id = :skinId AND skin_id != 0
        """, nativeQuery = true)
    List<String> findImagesBySkinId(int skinId);

    @Query(value = """
        SELECT imgPath
        FROM images
        WHERE car_id = :carId AND car_id != 0
        """, nativeQuery = true)
    List<String> findImagesByCarId(int carId);

    @Query(value = """
        SELECT imgPath
        FROM images
        WHERE track_id = :trackId AND track_id != 0
        """, nativeQuery = true)
    List<String> findImagesByTrackId(int trackId);
}
