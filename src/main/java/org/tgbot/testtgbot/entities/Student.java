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
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Size(max = 100)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @Column(name = "patronymic", length = 100)
    private String patronymic;

    @Size(max = 100)
    @Column(name = "faculty", length = 100)
    private String faculty;

    @Size(max = 100)
    @Column(name = "study_group", length = 100)
    private String studyGroup;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "student")
    private Set<Resident> residents = new LinkedHashSet<>();

}