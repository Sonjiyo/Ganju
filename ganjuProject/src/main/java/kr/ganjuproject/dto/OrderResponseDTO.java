package kr.ganjuproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private int restaurantTableNo;
    private List<OrderMenuDTO> orderMenus;
    private int price;
    private String content;
    private LocalDateTime regDate;
    private String division;
    private String uid;
}
