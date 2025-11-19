package org.retina.care.backend.di.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        final int SALT_LENGTH = 16;
        final int PARALLELISM_FACTOR = 1;
        final int ITERATIONS = 10;
        final int HASH_LENGTH_BYTES = 32;
        final int MEMORY_COST_KB = 60_000;

        return new Argon2PasswordEncoder(
                SALT_LENGTH,
                HASH_LENGTH_BYTES,
                PARALLELISM_FACTOR,
                MEMORY_COST_KB,
                ITERATIONS
        );
    }
}
