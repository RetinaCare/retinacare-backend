package org.retina.care.backend.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SignUpResponseDto {
    public UUID userId;
    public String fullname;
    public String email;
}
