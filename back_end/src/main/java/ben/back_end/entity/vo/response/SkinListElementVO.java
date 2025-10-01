package ben.back_end.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SkinListElementVO {
    private int skinId;
    private String skinName;
    private String skinAvatar;
    private String linkURL;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
}
