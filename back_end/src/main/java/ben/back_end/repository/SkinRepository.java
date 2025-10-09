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
            END,
            CASE
                WHEN :choice = 3 THEN 1
                ELSE 0
            END DESC
        """, nativeQuery = true)
    List<Skins> SearchSkins(
            @Param("tags") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取涂装列表
    @Query(value = """
        SELECT skins.*
        FROM skins
        JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND skins.skinId != 0
        ORDER BY skins.skinId ASC;
        """, nativeQuery = true)
    List<Skins> getSkinsById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = """
        SELECT skins.*
        FROM skins
        JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND skins.skinId != 0
        ORDER BY skins.createdAt ASC;
        """, nativeQuery = true)
    List<Skins> getSkinsByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = """
        SELECT skins.*
        FROM skins
        JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND skins.skinId != 0
        ORDER BY skins.createdAt DESC;
        """, nativeQuery = true)
    List<Skins> getSkinsByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = """
        SELECT skins.*
        FROM skins
        JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE tags.tagName = :tag
          AND skins.skinId != 0
        ORDER BY skins.views DESC;
        """, nativeQuery = true)
    List<Skins> getSkinsByPopular(String tag);

    // 获取最新涂装
    @Query(value = """
        SELECT skins.*
        FROM skins
        ORDER BY skins.createdAt DESC LIMIT 1;
        """, nativeQuery = true)
    Skins findNewestSkins();

    // 通过名称获取涂装
    @Query(value = """
        SELECT skins.*
        FROM skins
        WHERE skins.skinName = :skinName;
        """, nativeQuery = true)
    Skins findSkinByName(String skinName);
}

