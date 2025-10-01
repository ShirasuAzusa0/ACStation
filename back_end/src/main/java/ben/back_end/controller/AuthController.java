package ben.back_end.controller;

import ben.back_end.entity.RestBean;
import ben.back_end.entity.Users;
import ben.back_end.entity.dto.LoginDto;
import ben.back_end.entity.dto.RegisterDto;
import ben.back_end.entity.userType;
import ben.back_end.entity.vo.response.LoginVO;
import ben.back_end.entity.vo.response.RegisterVO;
import ben.back_end.repository.UserRepository;
import ben.back_end.util.JwtUtils;
import ben.back_end.util.RSAKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RSAKeyUtil rsaKeyUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterDto dto) {
        if (userRepository.findByEmail(dto.getAccount()) != null) {
            return ResponseEntity.badRequest().body(RestBean.failure("该邮箱已被注册"));
        }

        // 找到当前最大的userId
        int maxId = userRepository.findMaxId();
        int newId = maxId + 1;

        // 对密码进行解密后再加密（RSA解密->BCrypt加密）
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String rawPassword = dto.getPassword();
            // 先把空格替换回加号
            String fixedPassword = rawPassword.replace(' ', '+');
            // 再去除密码中所有空白字符（空格、换行等）
            String encryptedPasswordClean = fixedPassword.replaceAll("\\s", "");

            System.out.println(encryptedPasswordClean);

            // 再进行解密存储
            String decryptedPassword = rsaKeyUtil.decrypt(encryptedPasswordClean);
            String encryptedPassword = passwordEncoder.encode(decryptedPassword);

            dto.setPassword(encryptedPassword);
        }

        Users user = new Users();
        // 手动设置
        user.setUserId(newId);
        user.setUserName(dto.getUsername());
        user.setEmail(dto.getAccount());
        user.setPassword(dto.getPassword());
        user.setAvatar("https://avatars.githubusercontent.com/u/19370775");
        user.setCreatedAt(LocalDateTime.now());
        user.setLastConnectedAt(LocalDateTime.now());
        user.setType(userType.user);

        userRepository.save(user);

        // 生成 JWT 令牌
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        UserDetails userDetails = User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(auth.getAuthorities())
                .build();

        String token = jwtUtils.generateJWT(userDetails, user.getUserId(), dto.getUsername());
        String bearerToken = "Bearer " + token;
        RegisterVO vo = new RegisterVO();
        vo.setUsername(dto.getUsername());
        vo.setUserId(newId);
        vo.setToken(bearerToken);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "注册成功",
                        "data", vo
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute LoginDto dto) {
        if (userRepository.findByEmail(dto.getAccount()) == null) {
            return ResponseEntity.badRequest().body(RestBean.failure("不存在该账号"));
        }

        // 从application/json中获取邮箱
        String email = dto.getAccount();

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(RestBean.failure("该邮箱未注册账号"));
        }

        // 对密码进行解密
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String rawPassword = dto.getPassword();
            // 先把空格替换回加号
            String fixedPassword = rawPassword.replace(' ', '+');
            // 再去除密码中所有空白字符（空格、换行等）
            String encryptedPasswordClean = fixedPassword.replaceAll("\\s+", "");

            System.out.println(encryptedPasswordClean);

            // 再进行解密
            String decryptedPassword = rsaKeyUtil.decrypt(encryptedPasswordClean);

            // 使用BCrypt验证密码
            if (!passwordEncoder.matches(decryptedPassword, user.getPassword())) {
                return ResponseEntity.badRequest().body(RestBean.failure("密码错误"));
            }
        }

        user.setLastConnectedAt(LocalDateTime.now());
        userRepository.save(user);

        // 生成 JWT 令牌
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        UserDetails userDetails = User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(auth.getAuthorities())
                .build();

        String token = jwtUtils.generateJWT(userDetails, user.getUserId(), user.getUserName());
        String bearerToken = "Bearer " + token;
        LoginVO vo = new LoginVO();
        vo.setUsername(user.getUserName());
        vo.setUserId(user.getUserId());
        vo.setToken(bearerToken);

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "登录成功",
                        "data", vo
                )
        );
    }
}
