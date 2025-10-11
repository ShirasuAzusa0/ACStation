package ben.back_end.service;

import ben.back_end.entity.Plugins;
import ben.back_end.repository.PluginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluginService {
    @Autowired
    private PluginRepository pluginRepository;

    // 根据搜索和筛选tag查找相关视频信息实体
    public List<Plugins> getPluginBySearch(String tag, int choice, String search) {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search");
        }
        String[] parts = search.split(" ");
        String regex = String.join("|", parts);
        return pluginRepository.SearchPlugins(tag, regex, choice);
    }

    // 通过tag获取视频列表
    public List<Plugins> getPluginByTag(String tag, int choice) {
        return switch (choice) {
            case 1 -> pluginRepository.getPluginsById(tag);
            case 2 -> pluginRepository.getPluginsByNewest(tag);
            case 3 -> pluginRepository.getPluginsByOldest(tag);
            case 4 -> pluginRepository.getPluginsByPopular(tag);
            default -> null;
        };
    }

    // 获取所有插件
    public Plugins getNewestPlugins() {
        return pluginRepository.findNewestPlugins();
    }
}
