package com.books.bonnets.librarian.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
@Data // Lombok generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString //make use of exclude @ToString{exclude = "email"} to not show specific fields.
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "payment_status")
    private byte paymentStatus;

    public PurchaseOrder(Book book, Customer customer, LocalDate purchaseDate, byte paymentStatus) {
        this.book = book;
        this.customer = customer;
        this.purchaseDate = purchaseDate;
        this.paymentStatus = paymentStatus;
    }
}
