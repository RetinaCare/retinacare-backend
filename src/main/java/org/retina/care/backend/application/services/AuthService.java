package org.retina.care.backend.application.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.retina.care.backend.application.dto.auth.*;
import org.retina.care.backend.core.infra.security.AccessToken;
import org.retina.care.backend.core.infra.security.JwtService;
import org.retina.care.backend.domain.models.RefreshTokenEntity;
import org.retina.care.backend.domain.models.UserEntity;
import org.retina.care.backend.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // Transactional here ensures that if anything fails,
    // the user is not persisted to the database.
    @Transactional
    public SignUpResponseDto signup(SignUpRequestDto dto) throws Exception {
        // We do NOT want duplicate signups.
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("A user with this email address already exists");
        }

        // User creation and setting properties.
        // We MUST persist the password hash and not the password itself.
        UserEntity newUser = new UserEntity();

        newUser.setFullname(dto.getFullname());
        newUser.setEmail(dto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(newUser);

        // User has been persisted, next we generate access and
        // refresh tokens.
        AccessToken accessToken = this.jwtService.generateAccessToken(newUser.getFullname());
        RefreshTokenEntity refreshToken = this.refreshTokenService.createRefreshToken(newUser);

        // Final response, we should NOT get any issues.
        return new SignUpResponseDto(
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
    }

    public SignInResponseDto signin(SignInRequestDto dto) throws Exception {
        UserEntity savedUser = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Username or password is invalid"));

        // Password MUST match stored hash.
        if (!passwordEncoder.matches(dto.getPassword(), savedUser.getPasswordHash())) {
            // Being ambiguous for good measure against harmful parties ;)
            throw new BadRequestException("Username or password is invalid");
        }

        // As usual, access and refresh tokens are generated.
        AccessToken accessToken = this.jwtService.generateAccessToken(savedUser.getFullname());
        RefreshTokenEntity refreshToken = this.refreshTokenService.createRefreshToken(savedUser);

        // Final response.
        return new SignInResponseDto(
                savedUser.getUserId(),
                savedUser.getFullname(),
                savedUser.getEmail(),
                new AuthPayload(
                        "Bearer",
                        accessToken.getToken(),
                        refreshToken.getToken(),
                        accessToken.getExpiryDate(),
                        refreshToken.getExpiryDate()
                )
        );
    }
}
