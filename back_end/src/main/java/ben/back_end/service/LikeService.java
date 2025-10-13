package ben.back_end.service;

import ben.back_end.entity.Likes;
import ben.back_end.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    // 查询点赞情况
    public Likes CheckLike(int userId, String type, int targetId) {
        return likeRepository.findUserLike(userId, type, targetId);
    }

    // 点赞
    public void AddLike(int userId, String type, int targetId) {
        likeRepository.addLike(userId, type, targetId);
    }

    // 取消点赞
    public void RemoveLike(int userId, String type, int targetId) {
        likeRepository.removeLike(userId, type, targetId);
    }

}
