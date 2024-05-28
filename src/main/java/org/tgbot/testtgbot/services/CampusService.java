package org.tgbot.testtgbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tgbot.testtgbot.entities.*;
import org.tgbot.testtgbot.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CampusService {
    @Autowired
    private DormitoryRepository dormitoryRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ResidentRepository residentRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public Dormitory getDormitoryWithFloors(Integer dormitoryId) {
        Dormitory dormitory = dormitoryRepository.findById(dormitoryId).orElseThrow();
        dormitory.getFloors().size();
        return dormitory;
    }

    @Transactional
    public String getAvailableRoomsByDormName(String dormName) {
        Dormitory dormitory = dormitoryRepository.findByName(dormName);
        if (dormitory == null) {
            return "Общежития " + dormName + " нет в кампусе. /dormitories для просмотра всех общежитий";
        }
        StringBuilder answer = new StringBuilder("Свободные места в общежитии " + dormName + "\n\n");
        List<Floor> floors = new ArrayList<>(dormitory.getFloors());
        floors.sort(Comparator.comparing(Floor::getNumber));
        for (Floor floor : floors) {
            answer.append("        ");
            answer.append(floor.getNumber());
            answer.append(" этаж:\n");
            List<Room> rooms = new ArrayList<>(floor.getRooms());
            if (rooms.isEmpty()) {
                answer.append("Нет свободных комнат\n");
                continue;
            }
            for (Room room : rooms) {
                List<Resident> residents = residentRepository.findAllByRoom(room);
                if (residents.size() < room.getCapacity()) {
                    answer.append(room.getName());
                    answer.append(", свободных мест: ");
                    answer.append(room.getCapacity() - residents.size());
                    answer.append("\n");
                }
            }
        }
        return answer.toString();
    }

    public List<Dormitory> findAllDormitories() {
        return dormitoryRepository.findAll();
    }

    @Transactional
    public String getResidentsByDormAndRoomNumber(String dormName, String roomName) {
        Dormitory dormitory = dormitoryRepository.findByName(dormName);
        if (dormitory == null) {
            return "Общежития " + dormName + " нет в кампусе. /dormitories для просмотра всех общежитий";
        }

        Set<Floor> floors = dormitory.getFloors();
        for (Floor floor : floors) {
            for (Room room : floor.getRooms()) {
                if (room.getName().equals(roomName)) {
                    Set<Resident> residents = room.getResidents();
                    if (residents.isEmpty()) {
                        return "Нет проживающих в комнате " + roomName;
                    }
                    return residents.stream()
                            .map(resident -> {
                                Student student = resident.getStudent();
                                return student.getLastName() + " "
                                        + student.getFirstName() + " "
                                        + student.getPatronymic() + ", "
                                        + "Факультет " + student.getFaculty() + ", "
                                        + "Группа " + student.getStudyGroup();
                            })
                            .collect(Collectors.joining("\n"));
                }
            }
        }

        return "Комната не найдена";
    }
}
