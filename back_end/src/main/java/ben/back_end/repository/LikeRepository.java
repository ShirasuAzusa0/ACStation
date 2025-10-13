package ben.back_end.repository;

import ben.back_end.entity.Likes;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {

    // 查询用户是否已点赞过
    @Query(value = "SELECT * FROM likes WHERE userId = :userId AND type = :type AND cat_id = :targetId", nativeQuery = true)
    Likes findUserLike(int userId, String type, int targetId);

    // 点赞
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO likes (userId, type, cat_id) VALUES (:userId, :type, :targetId)", nativeQuery = true)
    void addLike(int userId, String type, int targetId);

    // 取消点赞
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE userId = :userId AND type = :type AND cat_id = :targetId", nativeQuery = true)
    void removeLike(int userId, String type, int targetId);
}
