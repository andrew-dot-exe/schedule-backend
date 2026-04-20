package ru.andrewexe.schedule.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import ru.andrewexe.schedule.dto.user.UserRegisterDto;
import ru.andrewexe.schedule.entity.User;
import ru.andrewexe.schedule.entity.UserRole;
import ru.andrewexe.schedule.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @Test
    void loadUserByUsernameUsesLoginLookupAndBuildsSpringSecurityUser() {
        when(repository.findByLogin("andrew")).thenReturn(Optional.of(
                new User(1L, UserRole.ADMIN, "andrew", "encoded-password")
        ));

        UserDetails result = service.loadUserByUsername("andrew");

        assertThat(result.getUsername()).isEqualTo("andrew");
        assertThat(result.getPassword()).isEqualTo("encoded-password");
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
        verify(repository).findByLogin("andrew");
    }

    @Test
    void loadUserByUsernameThrowsWhenLoginDoesNotExist() {
        when(repository.findByLogin("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Not found");
    }

    @Test
    void registerCreatesStudentUserWithEncodedPassword() {
        UserRegisterDto request = new UserRegisterDto("newuser", "secret-pass", "Andrew Example");
        when(repository.findByLogin("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret-pass")).thenReturn("encoded-password");
        when(repository.save(argThat(matchesUser(null, UserRole.STUDENT, "newuser",
                "encoded-password", "Andrew Example"))))
                .thenAnswer(invocation -> new User(11L, UserRole.STUDENT, "newuser",
                        "encoded-password", "Andrew Example"));

        UserDetails result = service.register(request);

        assertThat(result.getUsername()).isEqualTo("newuser");
        assertThat(result.getPassword()).isEqualTo("encoded-password");
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_STUDENT");
    }

    @Test
    void registerThrowsConflictWhenLoginAlreadyExists() {
        UserRegisterDto request = new UserRegisterDto("andrew", "secret-pass", "Andrew Example");
        when(repository.findByLogin("andrew")).thenReturn(Optional.of(
                new User(1L, UserRole.ADMIN, "andrew", "encoded-password", "Andrew Example")
        ));

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("409 CONFLICT")
                .hasMessageContaining("Login already exists");
    }

    private ArgumentMatcher<User> matchesUser(Long id, UserRole role, String login, String password, String name) {
        return user -> java.util.Objects.equals(user.getUsername(), login)
                && java.util.Objects.equals(user.getPassword(), password)
                && java.util.Objects.equals(user.getRole(), role.name())
                && java.util.Objects.equals(user.getName(), name);
    }
}
