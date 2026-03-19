package com.books.bonnets.librarian.Service;

import com.books.bonnets.librarian.entity.Customer;
import com.books.bonnets.librarian.repository.CustomerRepository;
import com.books.bonnets.librarian.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void findById_shouldReturnCustomer() {
        Customer customer = Customer.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("joe.doe@example.com")
                .phoneNumber("123456789")
                .build();

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(1);

        assertEquals(1, result.getId());
        verify(customerRepository).findById(1);
    }

    @Test
    void updateByUsername_shouldReturnCustomer(){
        String username = "joe.doe@example.com";

        Customer existingCustomer = Customer.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("joe.doe@example.com")
                .phoneNumber("123456789")
                .build();

        Customer incomingCustomer = Customer.builder()
                .id(1)
                .firstName("Johnny")
                .lastName("Doe")
                .email("joe.doe@example.com")
                .phoneNumber("123456789")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1)
                .firstName("Johnny")
                .lastName("Doe")
                .email("joe.doe@example.com")
                .phoneNumber("123456789")
                .build();

        when(customerRepository.findByEmail(username)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer result = customerService.updateByUsername(username, incomingCustomer);

        assertNotNull(result);
        assertEquals(savedCustomer, incomingCustomer);

        verify(customerRepository).findByEmail(username);
        verify(customerRepository).save(any(Customer.class));


    }

}
