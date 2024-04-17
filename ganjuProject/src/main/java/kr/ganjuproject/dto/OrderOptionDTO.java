package kr.ganjuproject.dto;

import lombok.Data;

@Data
public class OrderOptionDTO {
    private Long id;
    private String optionName;
    private int price;
}
