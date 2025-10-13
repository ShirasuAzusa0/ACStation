package ben.back_end.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CarDetailsVO {
    private int carId;
    private String carName;
    private String carAvatar;
    private int views;
    private int likes;
    private int downloads;
    private LocalDateTime createdAt;
    private String description;
    private List<String> images;
}
