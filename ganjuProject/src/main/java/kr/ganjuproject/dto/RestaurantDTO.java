package kr.ganjuproject.dto;

import lombok.Data;

@Data
public class RestaurantDTO {
    private String name;
    private String phone;
    private String addressFirst;
    private String addressElse;
    private int restaurantTable;
}
