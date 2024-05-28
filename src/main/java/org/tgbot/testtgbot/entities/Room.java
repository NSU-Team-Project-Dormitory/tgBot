package org.tgbot.testtgbot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_id_gen")
    @SequenceGenerator(name = "room_id_gen", sequenceName = "room_room_id_seq", allocationSize = 1)
    @Column(name = "room_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "room")
    private Set<Resident> residents = new LinkedHashSet<>();

}