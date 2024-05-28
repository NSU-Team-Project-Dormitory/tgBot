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
@Table(name = "floor")
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "floor_id_gen")
    @SequenceGenerator(name = "floor_id_gen", sequenceName = "floor_floor_id_seq", allocationSize = 1)
    @Column(name = "floor_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

    @Column(name = "number")
    private Integer number;

    @Size(max = 255)
    @Column(name = "image_path")
    private String imagePath;

    @OneToMany(mappedBy = "floor")
    private Set<Room> rooms = new LinkedHashSet<>();

}