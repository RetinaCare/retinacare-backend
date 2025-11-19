package org.retina.care.backend.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.retina.care.backend.application.dto.auth.SignUpRequestDto;
import org.retina.care.backend.application.dto.auth.SignUpResponseDto;
import org.retina.care.backend.application.services.AuthService;
import org.retina.care.backend.core.dto.HttpResponse;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Process user registration.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sign up successful."),
            @ApiResponse(responseCode = "400", description = "Client error with request."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<HttpResponse<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto dto) {
        UserEntity newUser = this.authService.create(dto);
        // ! TODO: Generate access and refresh tokens here
        SignUpResponseDto responseDto = new SignUpResponseDto(
                newUser.getUserId(),
                newUser.getFullname(),
                newUser.getEmail()
        );
        return new ResponseEntity<>(HttpResponse.Created("Sign up successful", responseDto),  HttpStatus.CREATED);
    }
}
