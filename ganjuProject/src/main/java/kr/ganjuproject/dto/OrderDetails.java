package kr.ganjuproject.dto;

import kr.ganjuproject.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetails {
    private Menu menu;
    private List<OptionDetails> optionDetailsList;
    private int quantity;
    private int totalPrice; // 총 가격 필드 추가

    // 생성자, getter 및 setter
    public OrderDetails(Menu menu, List<OptionDetails> optionDetailsList, int quantity) {
        this.menu = menu;
        this.optionDetailsList = optionDetailsList;
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        int menuPrice = menu.getPrice();
        int optionsPrice = optionDetailsList.stream()
                .mapToInt(optionDetail -> optionDetail.getMenuOptionValue().getPrice())
                .sum();
        this.totalPrice = (menuPrice + optionsPrice) * quantity;
    }

    public int getOptionsPriceSum() {
        return optionDetailsList.stream()
                .mapToInt(optionDetail -> optionDetail.getMenuOptionValue().getPrice())
                .sum();
    }
}
