package com.improvemyskills.productservice.web;

import com.improvemyskills.productservice.entity.Product;
import com.improvemyskills.productservice.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
@Log4j2
public class ProductController {

    ProductService productService;
    ProductConfig productConfig;

    public ProductController(ProductService productService, ProductConfig productConfig) {
        this.productService = productService;
        this.productConfig = productConfig;
    }

    @PostMapping("/products")
        //@PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<Product> postProduct(@RequestBody Product product){
        return ResponseEntity.ok(
                productService.save(product)
        );
    }

    @GetMapping("/products")
        //@PreAuthorize("hasAuthority('USER')")
    ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    @GetMapping("/products/{productId}")
        //@PreAuthorize("hasAuthority('USER')")
    ResponseEntity<Product> getProduct(@PathVariable Long productId){
        return ResponseEntity.ok(
                productService.getProduct(productId).orElse(null)
        );
    }

    @PostMapping("/products/bind/{orderId}")
    ResponseEntity<List<Product>> bindToAnOrder(@PathVariable String orderId, @RequestBody HashSet<Long> productIdList){

        return ResponseEntity.ok(
                productService.bindToAnOrder(orderId, productIdList)
        );
    }

    @GetMapping("/configs")
    ResponseEntity<String> getProductConfig(){
        log.info("alertThreshold {}", productConfig.getAlertThreshold());
        log.info("discountRate {}", productConfig.getDiscountRate());
		return ResponseEntity.ok(productConfig.getAlertThreshold() + " " + productConfig.getDiscountRate());
	}
}
