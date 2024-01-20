package org.voetsky.dispatcherBot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entites.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
