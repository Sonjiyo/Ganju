package kr.ganjuproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long menuId;
    private List<OptionSelection> selectedOptions;
    private int quantity; // 수량 필드 추가

    @Data
    public static class OptionSelection {
        private Long optionId;
        private Long valueId;
    }
}
