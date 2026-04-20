package ru.andrewexe.schedule.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "schedule_user")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    public User(Long id, UserRole role, String login, String password) {
        this(id, role, login, password, login);
    }

    public User(Long id, UserRole role, String login, String password, String name) {
        this.id = id;
        this.role = role;
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public User(String username, String password, List<SimpleGrantedAuthority> roles){
        this.login = username;
        this.password = password;
        this.name = username;
        roles = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // account never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // account credentials never expires
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ArrayList<String> getRoles() {
        return null;
    }

    public String getRole() {
        return role.toString();
    }

    public String getName() {
        return name == null ? login : name;
    }
}
