package org.retina.care.backend.application.services;

import org.retina.care.backend.domain.models.User;
import org.retina.care.backend.domain.repositories.UserRepository;
import org.retina.care.backend.dto.auth.SignUpRequestDto;
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

    public User create(SignUpRequestDto dto) {
        User user = new User();

        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());

        String passwordHash = passwordEncoder.encode(dto.getPassword());
        user.setPasswordHash(passwordHash);

        return userRepository.save(user);
    }
}
