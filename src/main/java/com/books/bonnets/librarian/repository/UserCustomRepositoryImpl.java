package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository{
    public EntityManager entityManager;

    @Override
    public void deleteByUsername(String username) {
        entityManager.createQuery("DELETE FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult());
    }
}
