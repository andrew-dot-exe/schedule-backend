package ru.andrewexe.schedule.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.andrewexe.schedule.entity.User;
import ru.andrewexe.schedule.entity.UserRole;
import ru.andrewexe.schedule.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

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
}
