package ben.back_end.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountDto {
    private String userName;
    private String email;
    private String avatar;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime lastConnectedAt;
}
