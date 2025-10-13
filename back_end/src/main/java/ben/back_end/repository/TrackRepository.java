package ben.back_end.repository;

import ben.back_end.entity.Tracks;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Tracks, Integer> {
    Tracks findById(int carId);

    @Query(value = """
            SELECT tracks.*
            FROM tracks
            WHERE tracks.trackId != 0
             AND (
               (:tag IS NULL OR :tag = '')
               OR tracks.trackId IN (
                   SELECT tag_relationships.track_id
                   FROM tag_relationships
                   JOIN tags ON tags.tagId = tag_relationships.tag_id
                   WHERE tags.tagName = :tag
               )
             )
            AND (
                    tracks.trackModName REGEXP :regex
                    OR tracks.description REGEXP :regex
                )
            ORDER BY
            CASE
                WHEN :choice = 1 THEN tracks.trackId
                WHEN :choice = 2 THEN tracks.createdAt
                WHEN :choice = 3 THEN tracks.createdAt
                WHEN :choice = 4 THEN tracks.views
            END,
            CASE
                WHEN :choice = 3 THEN 1
                ELSE 0
            END DESC
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
        LEFT JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND tracks.trackId != 0
        ORDER BY tracks.trackId ASC;
        """, nativeQuery = true)
    List<Tracks> getTracksById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        LEFT JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND tracks.trackId != 0
        ORDER BY tracks.createdAt ASC;
        """, nativeQuery = true)
    List<Tracks> getTracksByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        LEFT JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND tracks.trackId != 0
        ORDER BY tracks.createdAt DESC;
        """, nativeQuery = true)
    List<Tracks> getTracksByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        LEFT JOIN tag_relationships ON tracks.trackId = tag_relationships.track_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
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

    // 通过名称获取赛道MOD
    @Query(value = """
        SELECT tracks.*
        FROM tracks
        WHERE tracks.trackModName = :trackModName;
        """, nativeQuery = true)
    Tracks findTrackByModName(String trackModName);

    // 通过名称更新点击量
    @Query(value = """
        UPDATE tracks
        SET views = views + 1
        WHERE trackModName = :trackModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void updateViewsByName(String trackModName);

    // 通过名称获取点击量
    @Query(value = """
        SELECT tracks.views
        FROM tracks
        WHERE tracks.trackModName = :trackModName;
        """, nativeQuery = true)
    int getViewsByName(String trackModName);

    // 通过名称点赞数+1
    @Query(value = """
        UPDATE tracks
        SET likes = likes + 1
        WHERE trackModName = :trackModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void addLikesByName(String trackModName);

    // 通过名称点赞数-1
    @Query(value = """
        UPDATE tracks
        SET likes = likes - 1
        WHERE trackModName = :trackModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void removeLikesByName(String trackModName);

    // 通过名称获取点赞数
    @Query(value = """
        SELECT tracks.likes
        FROM tracks
        WHERE tracks.trackModName = :trackModName;
        """, nativeQuery = true)
    int getLikesByName(String trackModName);

    // 通过名称下载数+1
    @Query(value = """
        UPDATE tracks
        SET downloads = downloads + 1
        WHERE trackModName = :trackModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void addDownloadsByName(String trackModName);
}
