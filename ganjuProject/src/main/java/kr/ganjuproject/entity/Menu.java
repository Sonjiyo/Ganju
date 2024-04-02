package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;
    private String name;
    private int price;
    private String menuImage;
    private String info;
    @OneToMany(mappedBy = "id")
    @ToString.Exclude
    private List<MenuOption> options = new ArrayList<>();
}
