package ru.andrewexe.schedule.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.andrewexe.schedule.dto.user.UserCredentialsDto;
import ru.andrewexe.schedule.service.JwtService;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthController controller;

    @Test
    void loginAuthenticatesUserAndReturnsJwt() {
        byte[] password = "secret".getBytes(StandardCharsets.UTF_8);
        UserCredentialsDto request = new UserCredentialsDto("andrew", password);
        UserDetails user = User.withUsername("andrew")
                .password("encoded-password")
                .roles("ADMIN")
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("andrew", password));
        when(userDetailsService.loadUserByUsername("andrew")).thenReturn(user);
        when(jwtService.getToken(user)).thenReturn("jwt-token");

        String token = controller.login(request);

        assertThat(token).isEqualTo("jwt-token");

        ArgumentCaptor<Authentication> authenticationCaptor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(authenticationCaptor.capture());
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) authenticationCaptor.getValue();

        assertThat(authentication.getPrincipal()).isEqualTo("andrew");
        assertThat(authentication.getCredentials()).isEqualTo(password);
        verify(userDetailsService).loadUserByUsername("andrew");
        verify(jwtService).getToken(user);
    }
}
