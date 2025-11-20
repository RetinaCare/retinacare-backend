package org.retina.care.backend.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SignUpResponseDto {
    private UUID userId;
    private String fullname;
    private String email;
    private AuthPayload auth;
}