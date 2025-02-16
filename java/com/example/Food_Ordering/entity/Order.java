package com.example.Food_Ordering.entity;

import com.example.Food_Ordering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "orderNum")
    private String orderNum;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // A user places multiple orders
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    // One order can have multiple products
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<Product> products;
}
