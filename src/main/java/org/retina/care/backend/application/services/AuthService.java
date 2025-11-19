package org.retina.care.backend.application.services;

import org.retina.care.backend.domain.models.UserEntity;
import org.retina.care.backend.domain.repositories.UserRepository;
import org.retina.care.backend.application.dto.auth.SignUpRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
       this.userRepository = userRepository;
       this.passwordEncoder = passwordEncoder;
    }

    public UserEntity create(SignUpRequestDto dto) {
        UserEntity user = new UserEntity();

        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());

        String passwordHash = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(passwordHash);

        return userRepository.save(user);
    }
}
