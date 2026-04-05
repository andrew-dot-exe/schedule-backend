package ru.andrewexe.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrewexe.schedule.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
