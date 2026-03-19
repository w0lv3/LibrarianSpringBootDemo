package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.User;
import com.books.bonnets.librarian.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password before saving
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    @Override
    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Transactional
    @Override
    public void saveRefreshToken(String username, String refreshToken, LocalDateTime expiresAt) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("User not found"));

        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiresAt(expiresAt);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public User findByRefreshToken(String refreshToken) {
        return userRepository.findAll().stream()
                .filter(user -> refreshToken.equals(user.getRefreshToken()))
                .filter(user -> user.getRefreshTokenExpiresAt() != null)
                .filter(user -> user.getRefreshTokenExpiresAt().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);
    }
}