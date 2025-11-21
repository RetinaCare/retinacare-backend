package org.retina.care.backend.application.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class RefreshRequestDto {
    @NotBlank(message = "User Id is required")
    @UUID(message = "Invalid user id")
    private String userId;

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
