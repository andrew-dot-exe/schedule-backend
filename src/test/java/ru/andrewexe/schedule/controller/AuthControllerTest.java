package ru.andrewexe.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.andrewexe.schedule.dto.user.UserRegisterDto;
import ru.andrewexe.schedule.service.JwtService;
import ru.andrewexe.schedule.service.UserService;
import ru.andrewexe.schedule.utility.JwtFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void loginAuthenticatesUserAndReturnsJwtTokenPayload() throws Exception {
        UserDetails user = User.withUsername("andrew")
                .password("encoded-password")
                .roles("ADMIN")
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        when(jwtService.getToken(user)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("andrew", "secret-pass"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("andrew", "secret-pass")
        );
        verify(jwtService).getToken(user);
    }

    @Test
    void loginReturnsBadRequestWhenCredentialsAreBlank() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("", ""))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationManager, jwtService);
    }

    @Test
    void registerCreatesUserAndReturnsJwtTokenPayload() throws Exception {
        UserRegisterDto request = new UserRegisterDto("newuser", "secret-pass", "Andrew Example");
        UserDetails user = User.withUsername("newuser")
                .password("encoded-password")
                .roles("STUDENT")
                .build();

        when(userService.register(request)).thenReturn(user);
        when(jwtService.getToken(user)).thenReturn("registered-jwt");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("registered-jwt"));

        verify(userService).register(request);
        verify(jwtService).getToken(user);
    }

    @Test
    void registerReturnsConflictWhenLoginAlreadyExists() throws Exception {
        UserRegisterDto request = new UserRegisterDto("existing", "secret-pass", "Andrew Example");

        when(userService.register(request))
                .thenThrow(new ResponseStatusException(CONFLICT, "Login already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    private record LoginRequest(String login, String password) {
    }
}
