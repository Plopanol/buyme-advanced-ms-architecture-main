package com.improvemyskills.orderservice.web;

import com.improvemyskills.orderservice.entity.Order;
import com.improvemyskills.orderservice.service.OrderService;
import com.improvemyskills.orderservice.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<Order> getOrderById(@PathVariable String id){
        return ResponseEntity.ok(
                orderService.getOrderById(id).orElse(null)
        );
    }
    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }
    @PostMapping("/orders/purshase/{customerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<Order> purshase(@PathVariable Long customerId, @RequestBody HashSet<Long> productIdList){
        if (productIdList == null || productIdList.isEmpty()){
            throw new IllegalArgumentException("Param productIdList cannot be null or empty");
        }
        return ResponseEntity.ok(
                orderService.purchase(customerId, productIdList)
        );
    }

    @GetMapping("/orders/getbycustomerid/{customerId}")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<List<Order>> getOrderByCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(
                orderService.getOrderByCustomer(customerId)
        );
    }
}
