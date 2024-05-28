package org.tgbot.testtgbot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.testtgbot.entities.Dormitory;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Integer> {
    Dormitory findByName(String name);
}
