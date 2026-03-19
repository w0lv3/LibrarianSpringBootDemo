package com.books.bonnets.librarian.controller;

import com.books.bonnets.librarian.dto.UserDto;
import com.books.bonnets.librarian.entity.User;
import com.books.bonnets.librarian.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {

        if (userService.findByUsername(userDto.username()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        User user = User.builder()
                .username(userDto.username())
                .password(userDto.password())
                .email(userDto.email())
                .roles(userDto.roles())
                .build();

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping()
    public ResponseEntity<Void> deleteUserByUsername(Authentication auth) {
        userService.deleteByUsername(auth.getName());
        return ResponseEntity.noContent().build();
    }
}