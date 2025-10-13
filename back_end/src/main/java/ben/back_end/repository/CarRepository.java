package ben.back_end.repository;

import ben.back_end.entity.Cars;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Cars, Integer> {
    Cars findById(int carId);

    @Query(value = """
            SELECT cars.*
            FROM cars
            WHERE cars.carId != 0
             AND (
               (:tag IS NULL OR :tag = '')
               OR cars.carId IN (
                   SELECT tag_relationships.car_id
                   FROM tag_relationships
                   JOIN tags ON tags.tagId = tag_relationships.tag_id
                   WHERE tags.tagName = :tag
               )
             )
            AND (
                    cars.carModName REGEXP :regex
                    OR cars.description REGEXP :regex
                )
            ORDER BY
            CASE
                WHEN :choice = 1 THEN cars.carId
                WHEN :choice = 2 THEN cars.createdAt
                WHEN :choice = 3 THEN cars.createdAt
                WHEN :choice = 4 THEN cars.views
            END,
            CASE
                WHEN :choice = 3 THEN 1
                ELSE 0
            END DESC
        """, nativeQuery = true)
    List<Cars> SearchCars(
            @Param("tags") String tag,
            @Param("regex") String regex,
            @Param("choice") int choice
    );

    // 按Id顺序获取车辆MOD列表
    @Query(value = """
        SELECT cars.*
        FROM cars
        LEFT JOIN tag_relationships ON cars.carId = tag_relationships.car_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND cars.carId != 0
        ORDER BY cars.carId ASC;
        """, nativeQuery = true)
    List<Cars> getCarsById(String tag);

    // 按照发布时间顺序获取车辆MOD列表（顺序）
    @Query(value = """
        SELECT cars.*
        FROM cars
        LEFT JOIN tag_relationships ON cars.carId = tag_relationships.car_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND cars.carId != 0
        ORDER BY cars.createdAt ASC;
        """, nativeQuery = true)
    List<Cars> getCarsByNewest(String tag);

    // 按照发布时间顺序获取车辆MOD列表（逆序）
    @Query(value = """
        SELECT cars.*
        FROM cars
        LEFT JOIN tag_relationships ON cars.carId = tag_relationships.car_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND cars.carId != 0
        ORDER BY cars.createdAt DESC;
        """, nativeQuery = true)
    List<Cars> getCarsByOldest(String tag);

    // 按照观看数获取车辆MOD列表
    @Query(value = """
        SELECT cars.*
        FROM cars
        LEFT JOIN tag_relationships ON cars.carId = tag_relationships.car_id
        LEFT JOIN tags ON tag_relationships.tag_id = tags.tagId
        WHERE (tags.tagName = :tag OR :tag IS NULL OR :tag = '')
          AND cars.carId != 0
        ORDER BY cars.views DESC;
        """, nativeQuery = true)
    List<Cars> getCarsByPopular(String tag);

    // 获取最新车辆MOD
    @Query(value = """
        SELECT cars.*
        FROM cars
        ORDER BY cars.createdAt DESC LIMIT 1;
        """, nativeQuery = true)
    Cars findNewestCars();

    // 通过名称获取车辆MOD
    @Query(value = """
        SELECT cars.*
        FROM cars
        WHERE cars.carModName = :carModName;
        """, nativeQuery = true)
    Cars findCarByModName(String carModName);

    // 通过名称更新点击量
    @Query(value = """
        UPDATE cars
        SET views = views + 1
        WHERE carModName = :carModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void updateViewsByName(String carModName);

    // 通过名称获取点击量
    @Query(value = """
        SELECT cars.views
        FROM cars
        WHERE cars.carModName = :carModName;
        """, nativeQuery = true)
    int getViewsByName(String carModName);

    // 通过名称点赞数+1
    @Query(value = """
        UPDATE cars
        SET likes = likes + 1
        WHERE carModName = :carModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void addLikesByName(String carModName);

    // 通过名称点赞数-1
    @Query(value = """
        UPDATE cars
        SET likes = likes - 1
        WHERE carModName = :carModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void removeLikesByName(String carModName);

    // 通过名称获取点赞数
    @Query(value = """
        SELECT cars.likes
        FROM cars
        WHERE cars.carModName = :carModName;
        """, nativeQuery = true)
    int getLikesByName(String carModName);

    // 通过名称下载数+1
    @Query(value = """
        UPDATE cars
        SET downloads = downloads + 1
        WHERE carModName = :carModName;
        """, nativeQuery = true)
    // 告诉 Spring 这是修改操作
    @Modifying
    // 开启事务，否则无法执行更新
    @Transactional
    void addDownloadsByName(String carModName);
}