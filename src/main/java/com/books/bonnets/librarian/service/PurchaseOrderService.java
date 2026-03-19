package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.PurchaseOrder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

public interface PurchaseOrderService {

    HashMap<Integer, PurchaseOrder> getPurchaseOrderCache();

    PurchaseOrder save(PurchaseOrder purchaseOrder);

    PurchaseOrder update(Integer orderId, PurchaseOrder purchaseOrder);

    PurchaseOrder updateByUsername(String username ,Integer orderId, PurchaseOrder purchaseOrder);

    PurchaseOrder createOrder(Integer bookId, Integer customerId);

    void deleteOrder(Integer orderId);

    void deleteOrder(String username, Integer orderId);

    Double getTotalPriceByUsername(String name);

    Double getTotalPriceByCustomerId(Integer customerId);

    void setPaid(Integer orderId);
}
