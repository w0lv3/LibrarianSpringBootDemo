package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.Book;
import com.books.bonnets.librarian.entity.Customer;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    HashMap<Integer, Customer> getCustomerCache();

    Customer findById(Integer id);

    Customer patch(Integer id, Map<String, Object> customerPayload);

    Customer save(Customer customer);

    void deleteById(Integer id);

    List<Customer> findAll();

    /**
     * Allows a user to update only their own customer details.
     *
     * @param username the username of the current user
     * @param customer the updated customer data
     */
    Customer updateByUsername(String username, Customer customer);

    /**
     * Allows a user to update only their own customer details.
     *
     * @param username the username of the current user
     */
    void deleteByUsername(String username);
}
