package org.tgbot.testtgbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.testtgbot.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
