package ben.back_end.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    private String userName;
    private String account;
    private String password;
    private String captcha;
    private String captchaKey;
}
