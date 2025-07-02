package com.improvemyskills.productservice;

import com.improvemyskills.productservice.entity.Product;
import com.improvemyskills.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class);
    }

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {
            productRepository.saveAll(
                    List.of(
                            Product.builder().price(1459.23).categoryId(1L).name("iPhone 16 Pro Max").build(),
                            Product.builder().price(899.99).categoryId(1L).name("Samsung Galaxy S24 Ultra").build(),
                            Product.builder().price(299.49).categoryId(2L).name("Xiaomi Redmi Note 13").build(),
                            Product.builder().price(1299.00).categoryId(3L).name("MacBook Air M3").build(),
                            Product.builder().price(2399.99).categoryId(3L).name("Dell XPS 15 OLED").build(),
                            Product.builder().price(89.99).categoryId(4L).name("Logitech MX Master 3S").build(),
                            Product.builder().price(59.99).categoryId(4L).name("Razer DeathAdder V2").build(),
                            Product.builder().price(499.00).categoryId(5L).name("PlayStation 5 Slim").build(),
                            Product.builder().price(479.00).categoryId(5L).name("Xbox Series X").build(),
                            Product.builder().price(199.99).categoryId(6L).name("JBL Charge 5 Bluetooth Speaker").build(),
                            Product.builder().price(349.99).categoryId(6L).name("Bose QuietComfort 45").build(),
                            Product.builder().price(29.99).categoryId(7L).name("Amazon Echo Dot (5th Gen)").build(),
                            Product.builder().price(149.99).categoryId(7L).name("Google Nest Hub Max").build(),
                            Product.builder().price(999.00).categoryId(8L).name("Canon EOS R50 Mirrorless Camera").build(),
                            Product.builder().price(649.00).categoryId(8L).name("Sony Alpha ZV-E10").build()
                    )
            );
        };
    }
}
