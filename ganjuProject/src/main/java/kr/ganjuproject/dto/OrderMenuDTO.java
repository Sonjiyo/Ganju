package kr.ganjuproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderMenuDTO {
    private Long id;
    private String menuName;
    private int quantity;
    private int price;
    private List<OrderOptionDTO> orderOptions;
}
