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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    void getTeacherByIdReturnsTeacherFromApiV1Route() throws Exception {
        when(teacherService.getTeacherById(7L)).thenReturn(
                new TeacherResponseDto(7L, "Docent", "Jane Doe", "+79991112233", "@jane")
        );

        mockMvc.perform(get("/api/v1/teacher/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(7L))
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.academicDegree").value("Docent"));
    }

    @Test
    void updateTeacherReturnsUpdatedTeacher() throws Exception {
        TeacherRequestDto request = new TeacherRequestDto(8L, "Professor", "Alice Smith",
                "+79990000001", "@alice");
        TeacherResponseDto response = new TeacherResponseDto(8L, "Professor", "Alice Smith",
                "+79990000001", "@alice");

        when(teacherService.updateTeacher(request)).thenReturn(response);

        mockMvc.perform(patch("/api/v1/teacher/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(8L))
                .andExpect(jsonPath("$.fullName").value("Alice Smith"));
    }

    @Test
    void deleteTeacherReturnsDeletedTeacher() throws Exception {
        TeacherRequestDto request = new TeacherRequestDto(9L, "Professor", "Delete Me",
                "+79993334455", "@delete");
        TeacherResponseDto response = new TeacherResponseDto(9L, "Professor", "Delete Me",
                "+79993334455", "@delete");

        when(teacherService.deleteTeacher(request)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/teacher/delete/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(9L))
                .andExpect(jsonPath("$.vkId").value("@delete"));
    }
}
