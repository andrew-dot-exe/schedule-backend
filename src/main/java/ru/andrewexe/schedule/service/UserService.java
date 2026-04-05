package ru.andrewexe.schedule.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.andrewexe.schedule.entity.User;
import ru.andrewexe.schedule.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).toList()
                // ВНИМАНИЕ: роли сделаны пока что заглушками, но их обязательно
                // нужно реализовать.
        );
    }
}
