package ben.back_end.service;

import ben.back_end.entity.Skins;
import ben.back_end.repository.SkinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkinService {
    @Autowired
    private SkinRepository skinRepository;

    // 根据搜索和筛选tag查找相关涂装信息实体
    public List<Skins> getSkinBySearch(String tag, int choice, String search) {
        if (tag == null || tag.trim().isEmpty() || search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search");
        }
        String[] parts = search.split(" ");
        String regex = String.join("|", parts);
        return skinRepository.SearchSkins(tag, regex, choice);
    }

    // 通过tag获取视频列表
    public List<Skins> getSkinByTag(String tag, int choice) {
        return switch (choice) {
            case 1 -> skinRepository.getSkinsById(tag);
            case 2 -> skinRepository.getSkinsByNewest(tag);
            case 3 -> skinRepository.getSkinsByOldest(tag);
            case 4 -> skinRepository.getSkinsByPopular(tag);
            default -> null;
        };
    }
}
