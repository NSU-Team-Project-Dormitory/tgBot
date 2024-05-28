package org.tgbot.testtgbot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "resident")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resident_id_gen")
    @SequenceGenerator(name = "resident_id_gen", sequenceName = "resident_note_id_seq", allocationSize = 1)
    @Column(name = "note_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Size(max = 50)
    @Column(name = "contract_number", length = 50)
    private String contractNumber;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_expire_date")
    private LocalDate contractExpireDate;

}