package org.tgbot.testtgbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.testtgbot.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
