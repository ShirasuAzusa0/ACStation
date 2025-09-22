package ben.back_end.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarDto {
    private int carId;
    private String carModName;
    private String carAvatar;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
    private String linkURL;
    private String description;
}
