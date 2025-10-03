package ben.back_end.controller;

import ben.back_end.entity.vo.response.CaptchaVO;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class CaptchaController {

    public static Map<String, Object> generateCaptcha() throws IOException {
        LineCaptcha lineCaptcha = new LineCaptcha(100, 40, 4, 40);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            lineCaptcha.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] captchaBytes = outputStream.toByteArray();
        return Map.of(
                "code", lineCaptcha.getCode(),
                "image", captchaBytes
        );
    }

    // 获取验证码图片传给前端
    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() {
        Map<String, Object> captcha;
        try {
             captcha = generateCaptcha();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] captchaBytes = (byte[]) captcha.get("image");
        String captchaCode = (String) captcha.get("code");

        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaCode(captchaCode);
        vo.setCaptchaBytes(captchaBytes);

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取验证码成功",
                        "data", vo
                )
        );
    }

}
