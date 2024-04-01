package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private int restaurantTable;
    private String logo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    @ToString.Exclude
    private Users user;
    @OneToMany(mappedBy = "id")
    @ToString.Exclude
    private List<RestaurantImage> images = new ArrayList<>();
    @OneToMany(mappedBy = "id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();
}
