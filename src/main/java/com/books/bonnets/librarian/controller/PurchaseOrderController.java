package com.books.bonnets.librarian.controller;

import com.books.bonnets.librarian.dto.PurchaseOrderDto;
import com.books.bonnets.librarian.entity.PurchaseOrder;
import com.books.bonnets.librarian.mapper.PurchaseOrderMapper;
import com.books.bonnets.librarian.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class PurchaseOrderController {
    // Handles purchase-related API endpoints
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/purchase_orders")
    public ResponseEntity<List<PurchaseOrderDto>> getAllPurchaseOrders() {
        List<PurchaseOrderDto> purchaseOrderDtos = purchaseOrderService.getPurchaseOrderCache().values().stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(purchaseOrderDtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/purchase_order/{orderId}")
    public ResponseEntity<PurchaseOrderDto> updateOrder(@PathVariable Integer orderId,
                                                        @RequestBody @Valid PurchaseOrderDto purchaseOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderDto);
        return ResponseEntity.ok(purchaseOrderMapper.toDto(purchaseOrderService.update(orderId, purchaseOrder)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/purchase_order/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        purchaseOrderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/purchase_order/{bookId}/{customerId}")
    public ResponseEntity<PurchaseOrderDto> createOrder(@PathVariable Integer bookId,
                                                        @PathVariable Integer customerId) {
        PurchaseOrder order = purchaseOrderService.createOrder(bookId, customerId);
        return ResponseEntity.ok(purchaseOrderMapper.toDto(order));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/purchase_order/{orderId}/me")
    public ResponseEntity<Void> deleteOrder(Authentication auth, @PathVariable Integer orderId) {
        purchaseOrderService.deleteOrder(auth.getName(), orderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/purchase_order/{orderId}/me")
    public ResponseEntity<PurchaseOrderDto> updateOrder(Authentication auth,
                                                        @PathVariable Integer orderId,
                                                        @RequestBody @Valid PurchaseOrderDto purchaseOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderDto);
        return ResponseEntity.ok(
                purchaseOrderMapper.toDto(
                        purchaseOrderService.updateByUsername(auth.getName(), orderId, purchaseOrder)
                )
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/purchase_order/total_price")
    public ResponseEntity<Double> getTotalPrice(Authentication auth) {
        return ResponseEntity.ok(purchaseOrderService.getTotalPriceByUsername(auth.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/purchase_order/total_price/{customerId}")
    public ResponseEntity<Double> getTotalPrice(@PathVariable Integer customerId) {
        return ResponseEntity.ok(purchaseOrderService.getTotalPriceByCustomerId(customerId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/purchase_order/set_paid/{orderId}")
    public ResponseEntity<Void> setPaid(@PathVariable Integer orderId) {
        purchaseOrderService.setPaid(orderId);
        return ResponseEntity.noContent().build();
    }
}