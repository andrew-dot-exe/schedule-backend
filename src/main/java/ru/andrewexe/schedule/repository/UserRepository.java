package ru.andrewexe.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrewexe.schedule.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
