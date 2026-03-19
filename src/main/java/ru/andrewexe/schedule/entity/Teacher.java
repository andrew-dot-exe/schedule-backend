package ru.andrewexe.schedule.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "degree")
    private String academicDegree;

    @Column (name = "fullname", nullable = false)
    private String fullName;

    @Column (name = "phoneNumber", nullable = true)
    private String phoneNumber;

    @Column(name = "vkId", nullable = true)
    private String vkId;

    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.ALL)
    private Set<Subject> subjects = new HashSet<>();
}
