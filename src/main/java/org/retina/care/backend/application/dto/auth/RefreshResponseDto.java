package org.retina.care.backend.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResponseDto {
    private AuthPayload auth;
}
