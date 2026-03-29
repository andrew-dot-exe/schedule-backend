package ru.andrewexe.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrewexe.schedule.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findFirstByName(String name);
}
