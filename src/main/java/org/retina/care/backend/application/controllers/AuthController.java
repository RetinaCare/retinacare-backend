package org.retina.care.backend.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.retina.care.backend.application.dto.auth.*;
import org.retina.care.backend.application.services.AuthService;
import org.retina.care.backend.core.dto.HttpResponse;
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
        try {
            SignUpResponseDto payload = this.authService.signup(dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(HttpResponse.Created("Sign up successful", payload));
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(HttpResponse.BadRequest("Bad Request", e.getMessage()));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.InternalError());
        }
    }


    @PostMapping("/signin")
    @Operation(summary = "Processes user sign-ins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sign in successful."),
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
    public ResponseEntity<HttpResponse<SignInResponseDto>> signIn(@Valid @RequestBody SignInRequestDto dto) {
        try {
            SignInResponseDto payload = this.authService.signin(dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(HttpResponse.Ok("Sign in successful", payload));
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(HttpResponse.BadRequest("Bad Request", e.getMessage()));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.InternalError());
        }
    }


    @PostMapping("/refresh")
    @Operation(summary = "Refresh access tokens.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully refreshed tokens"),
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
    public ResponseEntity<HttpResponse<RefreshResponseDto>> refreshAccess(@Valid @RequestBody RefreshRequestDto dto) {
        try {
           RefreshResponseDto payload = this.authService.refreshAccess(dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(HttpResponse.Ok("Successfully refreshed tokens", payload));
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(HttpResponse.BadRequest("Bad Request", e.getMessage()));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.InternalError());
        }
    }
}