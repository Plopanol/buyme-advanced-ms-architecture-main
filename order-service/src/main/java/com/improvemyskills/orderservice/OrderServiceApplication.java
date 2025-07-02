package com.improvemyskills.orderservice;

import com.improvemyskills.orderservice.entity.Order;
import com.improvemyskills.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class);
    }

    @Bean
    CommandLineRunner commandLineRunner(OrderRepository orderRepository){
        return args -> {
            orderRepository.saveAll(
                    List.of(
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.TRUE).customerId(1L).productIds(List.of(1L, 2L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.FALSE).customerId(2L).productIds(List.of(3L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.TRUE).customerId(3L).productIds(List.of(4L, 5L, 6L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.FALSE).customerId(4L).productIds(List.of(2L, 7L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.TRUE).customerId(5L).productIds(List.of(8L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.FALSE).customerId(6L).productIds(List.of(1L, 9L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.TRUE).customerId(7L).productIds(List.of(10L, 11L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.FALSE).customerId(8L).productIds(List.of(12L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.TRUE).customerId(9L).productIds(List.of(13L, 14L)).build(),
                            Order.builder().id(UUID.randomUUID().toString()).isPaid(Boolean.FALSE).customerId(10L).productIds(List.of(15L)).build()
                    )
            );
        };
    }
}
