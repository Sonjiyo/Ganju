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
public class MenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    @ToString.Exclude
    private Menu menu;

    @OneToMany(mappedBy = "menuOption", orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<MenuOptionValue> values = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RoleMenuOption menuOptionId;
}