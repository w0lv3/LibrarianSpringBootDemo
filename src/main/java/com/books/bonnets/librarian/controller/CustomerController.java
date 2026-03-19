package com.books.bonnets.librarian.controller;

import com.books.bonnets.librarian.dto.CustomerDto;
import com.books.bonnets.librarian.entity.Customer;
import com.books.bonnets.librarian.mapper.CustomerMapper;
import com.books.bonnets.librarian.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CustomerController {
    //Handles customer-related API endpoints
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    //Admin section
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customerDtos = customerService.getCustomerCache().values().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.findById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/customer")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        return ResponseEntity.ok(customerMapper.toDto(customerService.save(customer)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody @Valid CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        customer.setId(id);
        return ResponseEntity.ok(customerMapper.toDto(customerService.save(customer)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/customer/{id}")
    public ResponseEntity<CustomerDto> patchCustomer(@PathVariable Integer id, @RequestBody Map<String, Object> patchPayload) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.patch(id, patchPayload)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //User section
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/customer")
    public ResponseEntity<CustomerDto> updateCustomer(Authentication auth,
                                                            @RequestBody @Valid CustomerDto customerDto) {

        Customer customer = customerMapper.toEntity(customerDto);
        return ResponseEntity.ok(customerMapper.toDto(customerService.updateByUsername(auth.getName(), customer)));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/customer")
    public ResponseEntity<Void> deleteCustomer(Authentication auth) {

        customerService.deleteByUsername(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
