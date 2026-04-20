package ru.andrewexe.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.andrewexe.schedule.SecurityConfig;
import ru.andrewexe.schedule.dto.teacher.TeacherRequestDto;
import ru.andrewexe.schedule.dto.teacher.TeacherResponseDto;
import ru.andrewexe.schedule.service.JwtService;
import ru.andrewexe.schedule.service.TeacherService;
import ru.andrewexe.schedule.utility.JwtFilter;

import java.util.List;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@Import({SecurityConfig.class, JwtFilter.class, JwtService.class})
@TestPropertySource(properties = {
        "application.jwt.secret=test-jwt-secret-key-12345678901234567890",
        "application.jwt.expiration=PT1H"
})
class TeacherControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private TeacherService teacherService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getAllTeachersRequiresJwtAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/teacher/get"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(teacherService);
    }

    @Test
    void invalidJwtReturnsUnauthorizedWithoutInvokingController() throws Exception {
        mockMvc.perform(get("/api/v1/teacher/get")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(teacherService);
    }

    @Test
    void validJwtAllowsAccessToProtectedEndpoints() throws Exception {
        UserDetails user = authenticatedUser();
        String token = jwtService.getToken(user);
        TeacherRequestDto request = new TeacherRequestDto(null, "Professor", "Alice Smith",
                "+79990000001", "@alice");

        when(userDetailsService.loadUserByUsername("andrew")).thenReturn(user);
        when(teacherService.listTeachers()).thenReturn(List.of(
                new TeacherResponseDto(1L, "Professor", "Alice Smith", "+79990000001", "@alice")
        ));
        when(teacherService.createTeacher(request)).thenReturn(
                new TeacherResponseDto(2L, "Professor", "Alice Smith", "+79990000001", "@alice")
        );

        mockMvc.perform(get("/api/v1/teacher/get")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].Id").value(1L));

        mockMvc.perform(post("/api/v1/teacher/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(2L))
                .andExpect(jsonPath("$.fullName").value("Alice Smith"));
    }

    private UserDetails authenticatedUser() {
        return User.withUsername("andrew")
                .password("encoded-password")
                .roles("ADMIN")
                .build();
    }
}
