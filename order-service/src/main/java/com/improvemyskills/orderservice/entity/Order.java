package com.improvemyskills.orderservice.entity;

import com.improvemyskills.orderservice.models.Customer;
import com.improvemyskills.orderservice.models.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table(name = "`order`") // Utilise des backticks pour H2 -- non recommandé
@Table(name = "orders") // au lieu de "order"  recommandé
public class Order {
    @Id
    private String id;
    private Long customerId;
    private boolean isPaid;
    //@Transient
    private Collection<Long> productIds;
    @Transient
    private Customer customer;
    @Transient
    private Collection<Product> products;
}
