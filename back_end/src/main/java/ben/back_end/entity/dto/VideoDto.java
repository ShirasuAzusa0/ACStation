package ben.back_end.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoDto {
    private int videoId;
    private String videoTitle;
    private String videoAvatar;
    private int views;
    private LocalDateTime createdAt;
    private String linkURL;
    private String description;
}
