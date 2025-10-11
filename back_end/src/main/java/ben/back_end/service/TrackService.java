package ben.back_end.service;

import ben.back_end.entity.Tracks;
import ben.back_end.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {
    @Autowired
    private TrackRepository trackRepository;

    // 根据搜索和筛选tag查找相关视频信息实体
    public List<Tracks> getTrackBySearch(String tag, int choice, String search) {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search");
        }
        String[] parts = search.split(" ");
        String regex = String.join("|", parts);
        return trackRepository.SearchTracks(tag, regex, choice);
    }

    // 通过tag获取视频列表
    public List<Tracks> getTrackByTag(String tag, int choice) {
        return switch (choice) {
            case 1 -> trackRepository.getTracksById(tag);
            case 2 -> trackRepository.getTracksByNewest(tag);
            case 3 -> trackRepository.getTracksByOldest(tag);
            case 4 -> trackRepository.getTracksByPopular(tag);
            default -> null;
        };
    }

    // 获取所有赛道MOD
    public Tracks getNewestTracks() {
        return trackRepository.findNewestTracks();
    }

    // 通过名称获取赛道MOD
    public Tracks getTrackByName(String trackName) {
        return trackRepository.findTrackByModName(trackName);
    }
}
