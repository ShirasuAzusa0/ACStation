package ben.back_end.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// 视频列表的视图层，仅返回概览信息
@Data
@AllArgsConstructor
public class VideoListVO {
    private String videoTitle;
    private String videoAvatar;
    private String linkURL;
    private int views;
    private LocalDateTime createdAt;
}
