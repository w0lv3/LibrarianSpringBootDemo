package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.Customer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomerCustomRepositoryImpl implements CustomerCustomRepository {

    public EntityManager entityManager;

    @Override
    public Optional<Customer> findByEmail(String username) {
        return Optional.ofNullable(entityManager.createQuery("SELECT c FROM Customer c WHERE c.email = :username", Customer.class)
                .setParameter("username", username)
                .getSingleResult());
    }

}
