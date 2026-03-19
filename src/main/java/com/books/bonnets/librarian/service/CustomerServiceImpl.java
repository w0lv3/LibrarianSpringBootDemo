package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.Customer;
import com.books.bonnets.librarian.repository.CustomerRepository;
import com.books.bonnets.librarian.utils.exceptions.InvalidCustomerException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final HashMap<Integer, Customer> customerCache = new HashMap<>();

    public CustomerServiceImpl(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void prefillCache() {
        customerRepository.findAll().forEach(customer -> customerCache.put(customer.getId(), customer));
    }

    @Override
    public HashMap<Integer, Customer> getCustomerCache() {
        return customerCache;
    }

    @Transactional
    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new InvalidCustomerException("Customer not found!"));
    }

    @Transactional
    @Override
    public Customer save(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        customerCache.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
        userService.deleteById(id);
        customerCache.remove(id);
    }

    @Transactional
    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional
    @Override
    public Customer patch(Integer id, Map<String, Object> customerPayload) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new InvalidCustomerException("Customer not found!"));

        customerPayload.forEach((key, value) -> {
            switch (key) {
                case "firstName" -> customer.setFirstName((String) value);
                case "lastName" -> customer.setLastName((String) value);
                case "email" -> customer.setEmail((String) value);
                case "phoneNumber" -> customer.setPhoneNumber((String) value);
            }
        });

        Customer savedCustomer = customerRepository.save(customer);
        customerCache.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Transactional
    @Override
    public Customer updateByUsername(String username, Customer customer) {
        Customer existingCustomer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new InvalidCustomerException("Customer not found!"));
        customer.setId(existingCustomer.getId());

        Customer savedCustomer = customerRepository.save(customer);
        customerCache.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        Customer existingCustomer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new InvalidCustomerException("Customer not found!"));
        userService.deleteById(existingCustomer.getId());
        customerRepository.deleteById(existingCustomer.getId());
        customerCache.remove(existingCustomer.getId());
    }
}