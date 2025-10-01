package ben.back_end.repository;

import ben.back_end.entity.Skins;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkinRepository extends JpaRepository<Skins, Integer> {
    Skins findById(int skinId);

    @Query(value = """
            SELECT skins.*
            FROM skins
            JOIN tags ON skins.skinId = tags.skin_id
            WHERE tags.tagName = :tag
            AND (
                    skins.skinName REGEXP :regex
                    OR skins.description REGEXP :regex
                )
            ORDER BY
            CASE
                WHEN :choice = 1 THEN skins.skinId
                WHEN :choice = 2 THEN skins.createdAt
                WHEN :choice = 3 THEN skins.createdAt
                WHEN :choice = 4 THEN skins.views
            END
            CASE
                WHEN :choice = 3 THEN DESC
                ELSE ASC
            END
        """, nativeQuery = true)
    List<Skins> SearchSkins(
            @Param("tags") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取涂装列表
    @Query(value = "SELECT skins.* FROM skins JOIN tags ON skins.skinId = tags.skin_id WHERE tagName = :tag ORDER BY skins.skinId ASC", nativeQuery = true)
    List<Skins> getSkinsById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = "SELECT skins.* FROM skins JOIN tags ON skins.skinId = tags.skin_id WHERE tagName = :tag ORDER BY skins.createdAt ASC", nativeQuery = true)
    List<Skins> getSkinsByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = "SELECT skins.* FROM skins JOIN tags ON skins.skinId = tags.skin_id WHERE tagName = :tag ORDER BY skins.createdAt DESC", nativeQuery = true)
    List<Skins> getSkinsByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = "SELECT skins.* FROM skins JOIN tags ON skins.skinId = tags.skin_id WHERE tagName = :tag ORDER BY skins.views ASC", nativeQuery = true)
    List<Skins> getSkinsByPopular(String tag);
}

