package ben.back_end.repository;

import ben.back_end.entity.Plugins;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PluginRepository extends JpaRepository<Plugins, Integer> {
    Plugins findById(int carId);

    @Query(value = """
            SELECT plugins.*
            FROM plugins
            WHERE plugins.pluginId != 0
             AND (
               (:tag IS NULL OR :tag = '')
               OR plugins.pluginId IN (
                   SELECT tag_relationships.plugin_id
                   FROM tag_relationships
                   JOIN tags ON tags.tagId = tag_relationships.tag_id
                   WHERE tags.tagName = :tag
               )
             )
            AND (
                    plugins.pluginName REGEXP :regex
                    OR plugins.description REGEXP :regex
                )
            ORDER BY
            CASE
                WHEN :choice = 1 THEN plugins.pluginId
                WHEN :choice = 2 THEN plugins.createdAt
                WHEN :choice = 3 THEN plugins.createdAt
                WHEN :choice = 4 THEN plugins.views
            END,
            CASE
                WHEN :choice = 3 THEN 1
                ELSE 0
            END DESC
        """, nativeQuery = true)
    List<Plugins> SearchPlugins(
            @Param("tags") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取涂装列表
    @Query(value = """
        SELECT plugins.*
        FROM plugins
        LEFT JOIN tag_relationships ON plugins.pluginId = tag_relationships.plugin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND plugins.pluginId != 0
        ORDER BY plugins.pluginId ASC;
        """, nativeQuery = true)
    List<Plugins> getPluginsById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = """
        SELECT plugins.*
        FROM plugins
        LEFT JOIN tag_relationships ON plugins.pluginId = tag_relationships.plugin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND plugins.pluginId != 0
        ORDER BY plugins.createdAt ASC;
        """, nativeQuery = true)
    List<Plugins> getPluginsByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = """
        SELECT plugins.*
        FROM plugins
        LEFT JOIN tag_relationships ON plugins.pluginId = tag_relationships.plugin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND plugins.pluginId != 0
        ORDER BY plugins.createdAt DESC;
        """, nativeQuery = true)
    List<Plugins> getPluginsByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = """
        SELECT plugins.*
        FROM plugins
        LEFT JOIN tag_relationships ON plugins.pluginId = tag_relationships.plugin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND plugins.pluginId != 0
        ORDER BY plugins.views DESC;
        """, nativeQuery = true)
    List<Plugins> getPluginsByPopular(String tag);

    // 获取最新插件
    @Query(value = """
        SELECT plugins.*
        FROM plugins
        ORDER BY plugins.createdAt DESC LIMIT 1;
        """, nativeQuery = true)
    Plugins findNewestPlugins();
}
