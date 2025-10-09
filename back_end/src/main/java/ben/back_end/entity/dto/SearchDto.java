package ben.back_end.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDto {
    String search;
    String tag;
    int choice;
}
