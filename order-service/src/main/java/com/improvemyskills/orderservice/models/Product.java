package com.improvemyskills.orderservice.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private Long categoryId;
    private double price;
    private double discount;
    private String orderId;
}
