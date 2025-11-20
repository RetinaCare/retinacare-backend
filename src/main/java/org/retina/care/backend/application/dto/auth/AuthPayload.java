package org.retina.care.backend.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthPayload {
    private String type; // default should be Bearer
    private String accessToken;
    private String refreshToken;
}
