package com.books.bonnets.librarian.mapper;

import com.books.bonnets.librarian.dto.PurchaseOrderDto;
import com.books.bonnets.librarian.entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "customer.id", target = "customerId")
    PurchaseOrderDto toDto(PurchaseOrder purchaseOrder);

    @Mapping(source = "bookId", target = "book.id")
    @Mapping(source = "customerId", target = "customer.id")
    PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto);
}
