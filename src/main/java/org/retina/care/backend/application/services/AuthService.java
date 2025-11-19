package org.retina.care.backend.application.services;

import org.retina.care.backend.domain.models.User;
import org.retina.care.backend.domain.repositories.UserRepository;
import org.retina.care.backend.dto.auth.SignUpRequestDto;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
       this.userRepository = userRepository;
    }

    public User create(SignUpRequestDto dto) {
        User user = new User();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        String passwordHash = "";
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }
}
