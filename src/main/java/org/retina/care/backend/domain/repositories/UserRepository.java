package org.retina.care.backend.domain.repositories;

import org.retina.care.backend.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
