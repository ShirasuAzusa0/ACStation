package ben.back_end.repository;

import ben.back_end.entity.Skins;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkinRepository extends JpaRepository<Skins, Integer> {
    Skins findById(int skinId);

    @Query(value = """
            SELECT skins.*
            FROM skins
            WHERE skins.skinId != 0
             AND (
               (:tag IS NULL OR :tag = '')
               OR skins.skinId IN (
                       SELECT tag_relationships.skin_id
                       FROM tag_relationships
                       JOIN tags ON tags.tag_id = tag_relationships.tag_id
                       WHERE tags.tagName = :tag
               )
            )
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
        LEFT JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND skins.skinId != 0
        ORDER BY skins.skinId ASC;
        """, nativeQuery = true)
    List<Skins> getSkinsById(String tag);

    // 按照发布时间顺序获取视频列表（顺序）
    @Query(value = """
        SELECT skins.*
        FROM skins
        LEFT JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND skins.skinId != 0
        ORDER BY skins.createdAt ASC;
        """, nativeQuery = true)
    List<Skins> getSkinsByNewest(String tag);

    // 按照发布时间顺序获取视频列表（逆序）
    @Query(value = """
        SELECT skins.*
        FROM skins
        LEFT JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND skins.skinId != 0
        ORDER BY skins.createdAt DESC;
        """, nativeQuery = true)
    List<Skins> getSkinsByOldest(String tag);

    // 按照观看数获取视频列表
    @Query(value = """
        SELECT skins.*
        FROM skins
        LEFT JOIN tag_relationships ON skins.skinId = tag_relationships.skin_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
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

    // 通过名称更新点击量
    @Query(value = """
        UPDATE skins
        SET views = views + 1
        WHERE skinName = :skinName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void updateViewsByName(String skinName);

    // 通过名称获取点击量
    @Query(value = """
        SELECT skins.views
        FROM skins
        WHERE skins.skinName = :skinName;
        """, nativeQuery = true)
    int getViewsByName(String skinName);

    // 通过名称点赞数+1
    @Query(value = """
        UPDATE skins
        SET likes = likes + 1
        WHERE skinName = :skinName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void addLikesByName(String skinName);

    // 通过名称点赞数-1
    @Query(value = """
        UPDATE skins
        SET likes = likes - 1
        WHERE skinName = :skinName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void removeLikesByName(String skinName);

    // 通过名称获取点赞数
    @Query(value = """
        SELECT skins.likes
        FROM skins
        WHERE skins.skinName = :skinName;
        """, nativeQuery = true)
    int getLikesByName(String skinName);

    // 通过名称下载数+1
    @Query(value = """
        UPDATE skins
        SET downloads = downloads + 1
        WHERE skinName = :skinName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void addDownloadsByName(String skinName);
}

