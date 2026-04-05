package ru.andrewexe.schedule.dto.subject;

import ru.andrewexe.schedule.entity.Teacher;

import java.util.Set;

public record AddSubjectRequestDto(String name, Set<Teacher> teachers) {

}
