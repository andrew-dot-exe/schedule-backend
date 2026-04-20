package ru.andrewexe.schedule.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.andrewexe.schedule.dto.user.UserRegisterDto;
import ru.andrewexe.schedule.entity.User;
import ru.andrewexe.schedule.entity.UserRole;
import ru.andrewexe.schedule.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return toUserDetails(user);
    }

    public UserDetails register(UserRegisterDto request) {
        if (repository.findByLogin(request.login()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already exists");
        }

        User savedUser = repository.save(new User(
                null,
                UserRole.STUDENT,
                request.login(),
                passwordEncoder.encode(request.password()),
                request.name()
        ));
        return toUserDetails(savedUser);
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
