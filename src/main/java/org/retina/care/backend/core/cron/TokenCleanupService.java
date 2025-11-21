package org.retina.care.backend.core.cron;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.retina.care.backend.domain.repositories.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Runs the cleanup task every 1 hour.
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanExpiredRefreshTokens() {
        logger.info("Starting scheduled cleanup of expired refresh tokens.");
        refreshTokenRepository.deleteExpiredTokens(Instant.now());
        logger.info("Finished scheduled cleanup.");
    }
}