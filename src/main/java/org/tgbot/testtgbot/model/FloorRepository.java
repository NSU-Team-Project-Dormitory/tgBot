package org.tgbot.testtgbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.testtgbot.entities.Floor;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Integer> {
}
