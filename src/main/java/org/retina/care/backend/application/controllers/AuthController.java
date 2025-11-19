package org.retina.care.backend.application.controllers;

import org.retina.care.backend.core.dto.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // ! TODO: Define service here

    @PostMapping("/signup")
    public ResponseEntity<HttpResponse<String>> signUp() {
        // ! TODO: Update response
    }
}
