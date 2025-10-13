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
        if (search == null || search.trim().isEmpty()) {
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

    // 获取所有的涂装
    public Skins getNewestSkins() {
        return skinRepository.findNewestSkins();
    }

    // 通过名称获取涂装
    public Skins getSkinByName(String skinName) {
        return skinRepository.findSkinByName(skinName);
    }

    // 通过名称更新数据
    public void updateByName(String method, String type, String skinName) {
        switch (type) {
            case "views": skinRepository.updateViewsByName(skinName); break;
            case "downloads": skinRepository.addDownloadsByName(skinName); break;
            case "likes": {
                if (method.equals("add")) skinRepository.addLikesByName(skinName);
                else if(method.equals("remove")) skinRepository.removeLikesByName(skinName);
                else throw new IllegalArgumentException("Invalid method");
            }
        }
    }

    // 通过名称获取更新字段数据
    public int getUpdatedByName(String type, String skinName) {
        return switch (type) {
            case "views" -> skinRepository.getViewsByName(skinName);
            case "likes" -> skinRepository.getLikesByName(skinName);
            default -> -1;
        };
    }
}
