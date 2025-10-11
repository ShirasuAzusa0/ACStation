package ben.back_end.entity.vo.response;

import lombok.Data;

@Data
public class RegisterVO {
    private int userId;
    private String username;
    private String avatar;
    private String token;
}
