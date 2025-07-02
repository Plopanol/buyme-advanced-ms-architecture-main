package com.improvemyskills.orderservice.service.impl;

import com.improvemyskills.orderservice.clients.CustomerRestClient;
import com.improvemyskills.orderservice.clients.ProductRestClient;
import com.improvemyskills.orderservice.entity.Order;
import com.improvemyskills.orderservice.models.Customer;
import com.improvemyskills.orderservice.models.Product;
import com.improvemyskills.orderservice.repository.OrderRepository;
import com.improvemyskills.orderservice.service.OrderService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    OrderRepository orderRepository;
    CustomerRestClient customerRestClient;
    ProductRestClient productRestClient;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRestClient customerRestClient, ProductRestClient productRestClient) {
        this.orderRepository = orderRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order purchase(Long customerId, HashSet<Long> productIdList) {
        ResponseEntity<Customer> customerResponseEntity = customerRestClient.getCustomerById(customerId);
        if (!HttpStatus.OK.equals(customerResponseEntity.getStatusCode())){
            logger.error("The customer with the id %s does not exist", customerId);
            throw new NotFoundException(String.format("The customer with the id %s does not exist", customerId));
        }

        Order newOrder = orderRepository.save(
                Order.builder().id(UUID.randomUUID().toString())
                        .customerId(customerId).isPaid(Boolean.TRUE).build()
        );

        ResponseEntity<List<Product>> bindToAnOrderResponse = productRestClient.bindToAnOrder(newOrder.getId(), productIdList);
        if (!HttpStatus.OK.equals(bindToAnOrderResponse.getStatusCode())){
            logger.error("An error occured while trying to bind products to the correspondant order");
            throw new RuntimeException("An error occured while trying to bind products to the correspondant order");
        }
        newOrder.setProducts(bindToAnOrderResponse.getBody());

        return newOrder;
    }

    @Override
    public List<Order> getOrderByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
