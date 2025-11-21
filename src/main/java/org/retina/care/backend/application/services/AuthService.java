package org.retina.care.backend.application.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.retina.care.backend.application.dto.auth.*;
import org.retina.care.backend.core.infra.security.AccessToken;
import org.retina.care.backend.core.infra.security.JwtService;
import org.retina.care.backend.domain.models.RefreshTokenEntity;
import org.retina.care.backend.domain.models.UserEntity;
import org.retina.care.backend.domain.repositories.RefreshTokenRepository;
import org.retina.care.backend.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
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
        AccessToken accessToken = this.jwtService.generateAccessToken(newUser.getEmail());
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
        AccessToken accessToken = this.jwtService.generateAccessToken(savedUser.getEmail());
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

    @Transactional
    public RefreshResponseDto refreshAccess(@Valid RefreshRequestDto dto) throws Exception {
        // Firstly, we MUST check to see that this userId and refresh token pair
        // exists in our database.
        RefreshTokenEntity refreshToken = refreshTokenRepository
                .findByToken(dto.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Token expired or invalid"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            // Again, the ambiguity here is so that attackers don't know exactly
            // which is the case, whether invalid or expired.
            throw new BadRequestException("Token expired or invalid");
        }

        // A lot of chaining here, but essentially, if the userId string from
        // the refresh token in our database doesn't match the request userId,
        // we deny them refresh.
        if (!refreshToken.getUser().getUserId().toString().equals(dto.getUserId())) {
            logger.warn("UserId {} wrongly requested refresh with TokenId {}", dto.getUserId(), refreshToken.getRefreshTokenId());
            throw new BadRequestException("Token expired or invalid");
        }

        // Token rotation + refresh.
        AccessToken accessToken = this.jwtService.generateAccessToken(refreshToken.getUser().getEmail());
        RefreshTokenEntity newRefreshToken = this.refreshTokenService.createRefreshToken(refreshToken.getUser());

        // Invalidating expired tokens.
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.save(newRefreshToken);

        return new RefreshResponseDto(
                new AuthPayload(
                        "Bearer",
                        accessToken.getToken(),
                        newRefreshToken.getToken(),
                        accessToken.getExpiryDate(),
                        newRefreshToken.getExpiryDate()
                )
        );
    }
}
