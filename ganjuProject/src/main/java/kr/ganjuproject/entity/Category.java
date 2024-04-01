package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int turn;
    private String name;
    @OneToMany(mappedBy="id")
    @ToString.Exclude
    private List<Menu> menus = new ArrayList<>();
}
