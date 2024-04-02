package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
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
