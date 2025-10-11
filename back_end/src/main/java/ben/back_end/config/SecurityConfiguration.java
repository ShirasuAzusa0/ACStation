package ben.back_end.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                              // 前后端分离项目一般禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/auth/**").permitAll()               // 允许匿名访问 /auth 其下属所有页面
                        .requestMatchers("/api/key").permitAll()                  // 允许匿名访问 /key
                        .requestMatchers("/api/categories").permitAll()           // 允许匿名访问 /categories
                        .requestMatchers("/api/source/**").permitAll()            // 允许匿名访问 /source 其下属所有页面
                        .requestMatchers("/api/newest").permitAll()               // 允许匿名访问 /newest
                        .requestMatchers("/api/captcha").permitAll()              // 允许匿名访问 /captcha
                        .requestMatchers("/img/**").permitAll()                   // 允许匿名访问 /img
                        .anyRequest().authenticated()                               // 其它请求需要认证
                );
        return http.build();
    }

    // CORS config: adjust origins in production
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));                          // dev: allow all origins
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
