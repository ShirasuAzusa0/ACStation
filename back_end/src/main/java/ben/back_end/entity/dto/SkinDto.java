package ben.back_end.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkinDto {
    private int skinId;
    private String skinName;
    private String skinAvatar;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
    private String linkURL;
    private String description;
}
