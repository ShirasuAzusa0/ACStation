package ben.back_end.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagListElementVO {
    private int tagId;
    private String tagName;
}
