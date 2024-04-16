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
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;
    private int quantity;
    private int price; // 기본 가격 (1개 가격)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders_id")
    @ToString.Exclude
    private Orders order;

    // 총 가격은 계산 필드로, 직접 저장하지 않고 메소드로 계산해서 제공할 수 있음
    @Transient
    public int getTotalPrice() {
        // 옵션 가격을 포함한 총 가격 계산
        int optionPrice = orderOptions.stream().mapToInt(OrderOption::getPrice).sum();
        return (this.price + optionPrice) * this.quantity;
    }

    @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderOption> orderOptions = new ArrayList<>();
}
