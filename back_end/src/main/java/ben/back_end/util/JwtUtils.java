package ben.back_end.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtUtils {
    // 加密密钥
    @Value("${spring.security.jwt.key}")
    private String key;

    // JWT有效期设置
    @Value("${spring.security.jwt.expire}")
    private int expire;

    // JWT令牌过期时间计算方法
    public Date expireTime() {
        Calendar cal = Calendar.getInstance();
        // 登录后一周有效（7*24小时）
        cal.add(Calendar.HOUR, expire * 24);
        return cal.getTime();
    }

    // 解析用户信息的方法
    // 将已验证的DecodedJWT中自定义的用户信息提取出来，构造出UserInfo对象
    // 用户信息是提前设定好的，要是有变化还得回来这里修改添加新的信息
    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("userName").asString())
                .password("*****")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    // 解析用户Id
    // 从DecodedJWT中提取用户的是数据库主键或尾翼表示id，供业务层使用
    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    // 创建JWT令牌，需要用到用户的信息（从info中提取出来）、id、用户名
    public String generateJWT(UserDetails userDetails, int userId, String userName) {
        // 使用HMAC256加密算法
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                // 根据userInfo获取对应的信息出来
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire)
                .withIssuedAt(new Date())
                // 签名，最终得到JWT
                .sign(algorithm);
    }

    // JWT令牌验证与解析方法
    public DecodedJWT verifyJWT(String headerToken) {
        // 调用convertToken方法，去掉“Bearer”前缀，并校验token是否合法
        String token = this.convertToken(headerToken);
        if (token == null) { return null; }
        Algorithm algorithm = Algorithm.HMAC256(key);
        // 构造一个验证器，用来校验签名及标准字段
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            // 验证token的签名、iss、aud等，并抛出异常或返回解码后的DecodedJWT
            DecodedJWT jwt = verifier.verify(token);
            Date expiresAt = jwt.getExpiresAt();
            return new Date().after(expiresAt) ? null : jwt;
        } catch (JWTVerificationException e) {
            // 有异常统一返回null
            return null;
        }
    }

    // 格式化token方法，将headerToken的前缀去掉
    private String convertToken(String headerToken) {
        // 非空且token以Bearer作为前缀才是合法的token
        if (headerToken == null || headerToken.startsWith("Bearer ")) {
            return null;
        }
        return headerToken.substring(7);
    }
}