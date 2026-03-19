package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.Book;
import com.books.bonnets.librarian.entity.Customer;
import com.books.bonnets.librarian.entity.PurchaseOrder;
import com.books.bonnets.librarian.repository.PurchaseOrderRepository;
import com.books.bonnets.librarian.utils.commons.Enums;
import com.books.bonnets.librarian.utils.exceptions.BookNotAvailableException;
import com.books.bonnets.librarian.utils.exceptions.UnauthorizedAccessException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CustomerService customerService;
    private final BookService bookService;

    public HashMap<Integer, PurchaseOrder> purchaseOrderCache = new HashMap<>();

    @PostConstruct
    public void prefillCache(){
        purchaseOrderRepository.findAll().forEach(purchaseOrder -> {
            purchaseOrderCache.put(purchaseOrder.getId(), purchaseOrder);
        });
    }

    @Override
    public HashMap<Integer, PurchaseOrder> getPurchaseOrderCache(){
       return purchaseOrderCache;
    }

    @Transactional
    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        purchaseOrderCache.put(purchaseOrder.getId(), savedOrder);
        return savedOrder;
    }

    @Transactional
    @Override
    public PurchaseOrder update(Integer orderId, PurchaseOrder purchaseOrder) {
        if (orderId == null || purchaseOrder == null) {
            throw new IllegalArgumentException("OrderId and purchaseOrder cannot be null");
        }

        if(!purchaseOrderCache.containsKey(orderId))
            throw new IllegalArgumentException("Purchase order not found!");

        //Save/update the purchase order.
        PurchaseOrder savedOrder = getPurchaseOrder(orderId, purchaseOrder);
        purchaseOrderCache.put(purchaseOrder.getId(), savedOrder);
        return savedOrder;
    }

    @Transactional
    @Override
    public PurchaseOrder updateByUsername(String username, Integer orderId, PurchaseOrder purchaseOrder) {

        if(!purchaseOrderCache.containsKey(orderId))
            throw new IllegalArgumentException("Purchase order not found!");

        if (!purchaseOrderCache.get(orderId).getCustomer().getEmail().equals(username))
            throw new UnauthorizedAccessException("User does not have permission to update this purchase order!");

        //Save/update the purchase order.
        PurchaseOrder savedOrder = getPurchaseOrder(orderId, purchaseOrder);
        purchaseOrderCache.put(purchaseOrder.getId(), savedOrder);
        return savedOrder;
    }

    private PurchaseOrder getPurchaseOrder(Integer orderId, PurchaseOrder purchaseOrder) {
        purchaseOrder.setId(orderId);

        //Get the book and the customer from the cache.
        Book theBook = bookService.findById(purchaseOrder.getBook().getId());
        Customer theCustomer = customerService.findById(purchaseOrder.getCustomer().getId());
        purchaseOrder.setBook(theBook);
        purchaseOrder.setCustomer(theCustomer);

        //Save/update the purchase order.
        return purchaseOrder;
    }

    @Transactional
    @Override
    public PurchaseOrder createOrder(Integer bookId, Integer customerId) {

        //Make use of the OrElseThrow to remove the need to set theBook and theCustomer as Optional<>.
        Book theBook = bookService.findById(bookId);
        Customer theCustomer = customerService.findById(customerId);

        if (theBook.getQuantity() <= 0)
            throw new BookNotAvailableException("Book is not available for purchase!");

        theBook.setQuantity(theBook.getQuantity() - 1);
        if (theBook.getQuantity() == 0) {
            theBook.setAvailable(false);
        }
        bookService.save(theBook);

        PurchaseOrder thePurchaseOrder = save(new PurchaseOrder(theBook, theCustomer, LocalDate.now(Clock.systemUTC()), Enums.Status.ORDERED.getId()));
        //Add item to cache.
        purchaseOrderCache.put(thePurchaseOrder.getId(), thePurchaseOrder);
        return thePurchaseOrder;
    }

    @Transactional
    @Override
    public void deleteOrder(Integer orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Purchase order not found!"));
        purchaseOrderRepository.delete(purchaseOrder);
        purchaseOrderCache.remove(orderId);
    }

    @Transactional
    @Override
    public void deleteOrder(String username, Integer orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Purchase order not found!"));
        if (!purchaseOrder.getCustomer().getEmail().equals(username)) {
            throw new UnauthorizedAccessException("User does not have permission to delete this purchase order!");
        }
        purchaseOrderRepository.delete(purchaseOrder);
        purchaseOrderCache.remove(orderId);
    }

    @Transactional
    @Override
    public Double getTotalPriceByUsername(String username) {
        return purchaseOrderCache.values().stream()
                .filter(order -> order.getCustomer() != null)
                .filter(order -> username.equals(order.getCustomer().getEmail()))
                .filter(order -> order.getPaymentStatus() == Enums.Status.ORDERED.getId())
                .mapToDouble(order -> order.getBook().getPrice())
                .sum();
    }

    @Transactional
    @Override
    public Double getTotalPriceByCustomerId(Integer customerId) {
        return purchaseOrderCache.values().stream()
                .filter(order -> order.getCustomer() != null)
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .filter(order -> order.getPaymentStatus() == Enums.Status.ORDERED.getId())
                .mapToDouble(order -> order.getBook().getPrice())
                .sum();
    }

    @Transactional
    @Override
    public void setPaid(Integer orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Purchase order not found!"));
        purchaseOrder.setPaymentStatus(Enums.Status.PAID.getId());
        purchaseOrderRepository.save(purchaseOrder);
        purchaseOrderCache.put(orderId, purchaseOrder);
    }
}
