package ben.back_end.service;

import ben.back_end.entity.Videos;
import ben.back_end.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    // 根据搜索和筛选tag查找相关视频信息实体
    public List<Videos> getVideoBySearch(String tag, int choice, String search) {
        if (tag == null || tag.trim().isEmpty() || search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search");
        }
        String[] parts = search.split(" ");
        String regex = String.join("|", parts);
        return videoRepository.SearchVideos(tag, regex, choice);
    }

    // 通过tag获取视频列表
    public List<Videos> getVideosByTag(String tag, int choice) {
        return switch (choice) {
            case 1 -> videoRepository.getVideosById(tag);
            case 2 -> videoRepository.getVideosByNewest(tag);
            case 3 -> videoRepository.getVideosByOldest(tag);
            case 4 -> videoRepository.getVideosByPopular(tag);
            default -> null;
        };
    }
}
