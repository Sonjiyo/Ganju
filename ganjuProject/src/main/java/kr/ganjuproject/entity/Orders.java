package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int restaurantTableNo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;
    private int price;
    private LocalDateTime regDate;
    private String content;
    @OneToOne(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Review review;
    private String uid;
    @Enumerated(EnumType.STRING)
    private RoleOrders division;
}
