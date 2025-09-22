package ben.back_end.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PluginDto {
    private int pluginId;
    private String pluginName;
    private String pluginAvatar;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
    private String linkURL;
    private String description;
}
