package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName; // 옵션 이름
    private int price; // 옵션 추가 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_menu_id")
    private OrderMenu orderMenu;
}