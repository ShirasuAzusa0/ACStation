package ben.back_end.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PluginListElementVO {
    private int pluginId;
    private String pluginName;
    private String pluginAvatar;
    private String linkURL;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
}
