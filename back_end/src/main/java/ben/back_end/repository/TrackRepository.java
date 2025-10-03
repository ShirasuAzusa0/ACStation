package ben.back_end.repository;

import ben.back_end.entity.Tracks;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Tracks, Integer> {
    Tracks findById(int carId);

    @Query(value = """
            SELECT tracks.*
            FROM tracks
            JOIN tags ON tracks.trackId = tags.track_id
            WHERE tags.tagName = :tag
            AND (
                    tracks.trackName REGEXP :regex
                    OR tracks.description REGEXP :regex
                )
            ORDER BY
            CASE
                WHEN :choice = 1 THEN tracks.trackId
                WHEN :choice = 2 THEN tracks.createdAt
                WHEN :choice = 3 THEN tracks.createdAt
                WHEN :choice = 4 THEN tracks.views
            END
            CASE
                WHEN :choice = 3 THEN DESC
                ELSE ASC
            END
        """, nativeQuery = true)
    List<Tracks> SearchTracks(
            @Param("tags") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取涂装列表
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND tracks.trackId != 0
        ORDER BY tracks.trackId ASC;
        """, nativeQuery = true)
    List<Tracks> getTracksById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND tracks.trackId != 0
        ORDER BY tracks.createdAt ASC;
        """, nativeQuery = true)
    List<Tracks> getTracksByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND tracks.trackId != 0
        ORDER BY tracks.createdAt DESC;
        """, nativeQuery = true)
    List<Tracks> getTracksByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND tracks.trackId != 0
        ORDER BY tracks.views DESC;
        """, nativeQuery = true)
    List<Tracks> getTracksByPopular(String tag);

    // 获取最新赛道MOD
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        ORDER BY tracks.createdAt DESC LIMIT 1;
        """, nativeQuery = true)
    Tracks findNewestTracks();
}
