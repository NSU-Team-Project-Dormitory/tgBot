package org.tgbot.testtgbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.testtgbot.entities.Resident;
import org.tgbot.testtgbot.entities.Room;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    List<Resident> findAllByRoom(Room room);
}
