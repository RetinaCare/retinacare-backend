package org.retina.care.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @NotBlank(message = "Full name is required")
    @Size(min = 4, max = 128, message = "Fullname must be between 6-128 characters")
    public String fullname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 128, message = "Password must be between 6-128 characters")
    public String password;
}
