package ru.andrewexe.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.andrewexe.schedule.dto.teacher.TeacherRequestDto;
import ru.andrewexe.schedule.dto.teacher.TeacherResponseDto;
import ru.andrewexe.schedule.service.TeacherService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TeacherController(teacherService)).build();
    }

    @Test
    void getAllTeachersReturnsTeachersFromApiV1Route() throws Exception {
        when(teacherService.listTeachers()).thenReturn(List.of(
                new TeacherResponseDto(1L, "Professor", "Andrew Example", "+79990000000", "@andrew")
        ));

        mockMvc.perform(get("/api/v1/teacher/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].Id").value(1L))
                .andExpect(jsonPath("$[0].fullName").value("Andrew Example"))
                .andExpect(jsonPath("$[0].academicDegree").value("Professor"));
    }

    @Test
    void createTeacherReturnsCreatedTeacher() throws Exception {
        TeacherRequestDto request = new TeacherRequestDto(null, "Associate Professor", "Jane Doe",
                "+79991112233", "@jane");
        TeacherResponseDto response = new TeacherResponseDto(5L, "Associate Professor", "Jane Doe",
                "+79991112233", "@jane");

        when(teacherService.createTeacher(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/teacher/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(5L))
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.vkId").value("@jane"));
    }
}
