package ru.andrewexe.schedule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.andrewexe.schedule.dto.user.UserCredentialsDto;
import ru.andrewexe.schedule.dto.user.UserRegisterDto;
import ru.andrewexe.schedule.dto.user.UserTokenDto;
import ru.andrewexe.schedule.service.JwtService;
import ru.andrewexe.schedule.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public UserTokenDto login(@Valid @RequestBody UserCredentialsDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return new UserTokenDto(jwtService.getToken(user));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserTokenDto register(@Valid @RequestBody UserRegisterDto request) {
        UserDetails user = userService.register(request);
        return new UserTokenDto(jwtService.getToken(user));
    }
}
