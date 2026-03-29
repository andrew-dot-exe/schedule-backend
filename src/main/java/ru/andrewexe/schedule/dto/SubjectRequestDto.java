package ru.andrewexe.schedule.dto;

import ru.andrewexe.schedule.entity.Teacher;

import java.util.List;
import java.util.Set;

public record SubjectRequestDto(String name, Set<Teacher> teachers) {
}
