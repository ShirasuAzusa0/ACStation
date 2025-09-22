package ben.back_end.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrackDto {
    private int trackId;
    private String trackModName;
    private String trackAvatar;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
    private String linkURL;
    private String description;
}
