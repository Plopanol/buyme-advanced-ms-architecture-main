package com.improvemyskills.productservice.service;

import com.improvemyskills.productservice.entity.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product save(Product product);
    Optional<Product> getProduct(Long productId);
    List<Product> getAllProducts();
    List<Product> getProductByOrderId(String orderId);
    List<Product> bindToAnOrder(String orderId, HashSet<Long> productIdList);
}
