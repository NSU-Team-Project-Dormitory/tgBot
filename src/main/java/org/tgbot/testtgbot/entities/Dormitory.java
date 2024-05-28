package org.tgbot.testtgbot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dormitory")
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dormitory_id_gen")
    @SequenceGenerator(name = "dormitory_id_gen", sequenceName = "dormitory_dormitory_id_seq", allocationSize = 1)
    @Column(name = "dormitory_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "floor_count", nullable = false)
    private Integer floorCount;

    @Size(max = 255)
    @Column(name = "contact_info")
    private String contactInfo;

    @OneToMany(mappedBy = "dormitory")
    private Set<Floor> floors = new LinkedHashSet<>();

}