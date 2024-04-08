package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String content;
    private LocalDateTime regDate;
    @Enumerated(EnumType.STRING)
    private RoleCategory boardCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;
}
