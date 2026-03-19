package com.books.bonnets.librarian.mapper;

import com.books.bonnets.librarian.dto.CustomerDto;
import com.books.bonnets.librarian.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);
    Customer toEntity(CustomerDto customerDto);
}
