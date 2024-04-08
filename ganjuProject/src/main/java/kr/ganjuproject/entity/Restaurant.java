package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Builder
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
    @Builder.Default
    private int recognize = 0;
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
    @OneToMany(mappedBy = "id")
    @ToString.Exclude
    private List<Board> boards = new ArrayList<>();
    @OneToMany(mappedBy = "id")
    @ToString.Exclude
    private List<Menu> menus = new ArrayList<>();
}
