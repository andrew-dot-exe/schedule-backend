package ru.andrewexe.schedule.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Subject subject;
}
