package ben.back_end.entity.vo.response;

import lombok.Data;

@Data
public class CaptchaVO {
    String captchaCode;
    byte[] captchaBytes;
}
