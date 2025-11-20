package org.retina.care.backend.core.infra.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AccessToken {
    private String token;
    private Instant expiryDate;
}
