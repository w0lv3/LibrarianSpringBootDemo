package com.books.bonnets.librarian.controller;

import com.books.bonnets.librarian.dto.AuthResponseDto;
import com.books.bonnets.librarian.dto.LoginDto;
import com.books.bonnets.librarian.dto.RefreshTokenDto;
import com.books.bonnets.librarian.dto.RegisterUserDto;
import com.books.bonnets.librarian.entity.User;
import com.books.bonnets.librarian.service.JWTService;
import com.books.bonnets.librarian.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JWTService jwtService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        // Authenticate the user credentials against Spring Security.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.username(),
                        loginDto.password()
                )
        );

        // Load the user so the app can build JWTs and store refresh token data.
        User user = userService.findByUsername(loginDto.username());

        // Convert the application user into Spring Security's UserDetails format.
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(String[]::new))
                .build();

        // Generate access and refresh tokens for the authenticated user.
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Persist the refresh token so it can be validated later.
        userService.saveRefreshToken(
                user.getUsername(),
                refreshToken,
                LocalDateTime.now().plusDays(7)
        );

        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken, user.getUsername()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody @Valid RefreshTokenDto request) {
        // Find the user associated with the provided refresh token.
        User user = userService.findByRefreshToken(request.refreshToken());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Build UserDetails so the token can be revalidated.
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(String[]::new))
                .build();

        // Reject the request if the refresh token is no longer valid.
        if (!jwtService.isRefreshTokenValid(request.refreshToken(), userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Issue a fresh access token and refresh a token pair.
        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        // Replace the stored refresh token with the new one and extend expiry.
        userService.saveRefreshToken(
                user.getUsername(),
                newRefreshToken,
                LocalDateTime.now().plusDays(7)
        );

        return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken, user.getUsername()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        // Prevent duplicate usernames from being created.
        if (userService.findByUsername(registerUserDto.username()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        // Create a standard user account with the USER role.
        User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .email(registerUserDto.email())
                .roles(Set.of("ROLE_USER"))
                .build();

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
}