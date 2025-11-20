package org.retina.care.backend.core.infra.security;

import org.retina.care.backend.domain.models.UserEntity;
import org.retina.care.backend.domain.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // So SpringBoot is strange in that the method name is called 'loadUserByUsername'
    // but we can actually use any valid identifier, such as emails.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Build context user object, I'm using hardcoded authorities here,
        // which should be fine. At this current stage of the MVP, we don't have
        // admins, or superadmins.
        return User
                .builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_USER")
                .build();
    }
}
