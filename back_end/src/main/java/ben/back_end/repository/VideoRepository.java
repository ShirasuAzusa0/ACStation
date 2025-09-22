package ben.back_end.repository;

import ben.back_end.entity.Videos;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Videos, Integer> {
    Videos findById(int videoId);

    @Query(value = """
            SELECT videos.*
            FROM videos
            JOIN tags ON videos.videoId = tags.video_id
            WHERE tags.tagName = :tag
            AND (
                videos.videoTitle REGEXP :regex
                OR videos.description REGEXP :regex
              )
            ORDER BY
              CASE
                WHEN :choice = 1 THEN videos.videoId
                WHEN :choice = 2 THEN videos.createdAt
                WHEN :choice = 3 THEN videos.createdAt
                WHEN :choice = 4 THEN videos.views
              END
              CASE
                WHEN :choice = 3 THEN DESC
                ELSE ASC
              END
           """, nativeQuery = true)
    List<Videos> SearchVideos(
            @Param("tag") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取视频列表
    @Query(value = "SELECT videos.* FROM videos JOIN tags ON videos.videoId = tags.video_id WHERE tagName = :tag ORDER BY videos.videoId ASC", nativeQuery = true)
    List<Videos> getVideosById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = "SELECT videos.* FROM videos JOIN tags ON videos.videoId = tags.video_id WHERE tagName = :tag ORDER BY videos.createdAt ASC", nativeQuery = true)
    List<Videos> getVideosByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = "SELECT videos.* FROM videos JOIN tags ON videos.videoId = tags.video_id WHERE tagName = :tag ORDER BY videos.createdAt DESC", nativeQuery = true)
    List<Videos> getVideosByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = "SELECT videos.* FROM videos JOIN tags ON videos.videoId = tags.video_id WHERE tagName = :tag ORDER BY videos.views ASC", nativeQuery = true)
    List<Videos> getVideosByPopular(String tag);
}
