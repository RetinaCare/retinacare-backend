package org.retina.care.backend.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.retina.care.backend.application.dto.auth.AuthPayload;
import org.retina.care.backend.application.dto.auth.SignUpRequestDto;
import org.retina.care.backend.application.dto.auth.SignUpResponseDto;
import org.retina.care.backend.application.services.AuthService;
import org.retina.care.backend.application.services.RefreshTokenService;
import org.retina.care.backend.core.dto.HttpResponse;
import org.retina.care.backend.core.infrastructure.security.AccessToken;
import org.retina.care.backend.core.infrastructure.security.JwtService;
import org.retina.care.backend.domain.models.RefreshTokenEntity;
import org.retina.care.backend.domain.models.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication / Authorization")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Process user registration.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sign up successful."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Client error with request.",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = HttpResponse.class))
            )
    })
    public ResponseEntity<HttpResponse<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto dto) {
        UserEntity newUser = this.authService.signup(dto);

        AccessToken accessToken = this.jwtService.generateAccessToken(newUser.getFullname());
        RefreshTokenEntity refreshToken = this.refreshTokenService.createRefreshToken(newUser);

        SignUpResponseDto responseDto = new SignUpResponseDto(
                newUser.getUserId(),
                newUser.getFullname(),
                newUser.getEmail(),
                new AuthPayload(
                        "Bearer",
                        accessToken.getToken(),
                        refreshToken.getToken(),
                        accessToken.getExpiryDate(),
                        refreshToken.getExpiryDate()
                )
        );

        return new ResponseEntity<>(HttpResponse.Created("Sign up successful", responseDto),  HttpStatus.CREATED);
    }
}
