package com.improvemyskills.productservice.service.impl;

import com.improvemyskills.productservice.entity.Product;
import com.improvemyskills.productservice.repository.ProductRepository;
import com.improvemyskills.productservice.service.ProductService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProduct(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByOrderId(String orderId) {
        return productRepository.getByOrderId(orderId);
    }

    @Override
    public List<Product> bindToAnOrder(String orderId, HashSet<Long> productIdList) {
        List<Product> products = productRepository.findAllById(productIdList);
        if(products == null || products.isEmpty()){
            throw new RuntimeException("No product found from this productIdList");
/*            throw new FeignException.NotFound(
                    "No product found from this productIdList",
                    null, null, null
            );*/
        }
        List<Product> bindProducts = products.stream().map(p -> {
                    p.setOrderId(orderId);
                    return p;
                }).collect(Collectors.toList());

        return productRepository.saveAll(bindProducts);
    }
}
