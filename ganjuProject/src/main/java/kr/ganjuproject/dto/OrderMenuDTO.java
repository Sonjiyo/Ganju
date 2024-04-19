package kr.ganjuproject.dto;

import kr.ganjuproject.entity.OrderOption;
import lombok.Data;

import java.util.List;

@Data
public class OrderMenuDTO {
    private Long id;
    private String menuName;
    private int quantity;
    private int price;
    private List<OrderOptionDTO> orderOptions;

    public int getTotalPrice() {
        // 옵션 가격을 포함한 총 가격 계산
        int optionPrice = orderOptions.stream().mapToInt(OrderOptionDTO::getPrice).sum();
        return (this.price + optionPrice) * this.quantity;
    }
}
