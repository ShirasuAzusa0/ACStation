package ben.back_end.controller;

import ben.back_end.entity.dto.LoginDto;
import ben.back_end.util.JwtUtils;
import ben.back_end.util.RSAKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserReporitory userReporitory;

    @Resource
    private RSAKeyUtil rsaKeyUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute LoginDto dto) {}
}
