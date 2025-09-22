package ben.back_end.controller;

import ben.back_end.util.RSAKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/key")
public class RSAController {
    @Resource
    private RSAKeyUtil rsaKeyUtil;

    // 若路径只有 RequestMapping 中的那一部分，则此处的请求类型 Mapping 不写 URL
    @GetMapping
    public ResponseEntity<?> getPublicKey() {
        String public_key = rsaKeyUtil.getPublicKeyBase64();
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "公钥获取成功",
                        "public_key", public_key
                )
        );
    }
}
