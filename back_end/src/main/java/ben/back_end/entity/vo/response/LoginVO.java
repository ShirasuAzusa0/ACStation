package ben.back_end.entity.vo.response;

import lombok.Data;

@Data
public class LoginVO {
    private int userId;
    private String username;
    private String token;
}
