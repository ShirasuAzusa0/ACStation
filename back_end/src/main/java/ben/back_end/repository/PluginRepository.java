package ben.back_end.repository;

import ben.back_end.entity.Plugins;
import ben.back_end.entity.Tracks;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PluginRepository extends JpaRepository<Plugins, Integer> {
    Plugins findById(int carId);

    @Query(value = """
            SELECT plugins.*
            FROM plugins
            JOIN tags ON plugins.pluginId = tags.plugin_id
            WHERE tags.tagName = :tag
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
            END
            CASE
                WHEN :choice = 3 THEN DESC
                ELSE ASC
            END
        """, nativeQuery = true)
    List<Plugins> SearchPlugins(
            @Param("tags") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取涂装列表
    @Query(value = "SELECT plugins.* FROM plugins JOIN tags ON plugins.pluginId = tags.plugin_id WHERE tagName = :tag ORDER BY plugins.pluginId ASC", nativeQuery = true)
    List<Plugins> getPluginsById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = "SELECT plugins.* FROM plugins JOIN tags ON plugins.pluginId = tags.plugin_id WHERE tagName = :tag ORDER BY plugins.createdAt ASC", nativeQuery = true)
    List<Plugins> getPluginsByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = "SELECT plugin.* FROM plugins JOIN tags ON plugins.pluginId = tags.plugin_id WHERE tagName = :tag ORDER BY plugins.createdAt DESC", nativeQuery = true)
    List<Plugins> getPluginsByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = "SELECT plugins.* FROM tracks JOIN tags ON plugins.pluginId = tags.plugin_id WHERE tagName = :tag ORDER BY plugins.views ASC", nativeQuery = true)
    List<Plugins> getPluginsByPopular(String tag);
}
