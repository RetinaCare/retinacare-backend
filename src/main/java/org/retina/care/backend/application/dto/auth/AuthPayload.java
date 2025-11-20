package org.retina.care.backend.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AuthPayload {
    private String type; // default should be Bearer
    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpires;
    private Instant refreshTokenExpires;
}
