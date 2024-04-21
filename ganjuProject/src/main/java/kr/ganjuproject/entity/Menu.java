package kr.ganjuproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @JsonManagedReference
    private Category category;
    private String name;
    private int price;
    private String menuImage;
    private String info;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;
    @OneToMany(mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<MenuOption> options = new ArrayList<>();
    private int mainMenu = 0;
}
