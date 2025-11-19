package org.retina.care.backend.application.controllers;

import org.retina.care.backend.application.dto.auth.SignUpRequestDto;
import org.retina.care.backend.application.dto.auth.SignUpResponseDto;
import org.retina.care.backend.application.services.AuthService;
import org.retina.care.backend.core.dto.HttpResponse;
import org.retina.care.backend.domain.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpResponse<SignUpResponseDto>> signUp(SignUpRequestDto dto) {
        User newUser = this.authService.create(dto);
        // ! TODO: Generate access and refresh tokens here
        SignUpResponseDto responseDto = new SignUpResponseDto(
                newUser.getUserId(),
                newUser.getFullname(),
                newUser.getEmail()
        );
        return new ResponseEntity<>(HttpResponse.Ok("Sign-up successful", responseDto),  HttpStatus.OK);
    }
}
