package kr.ganjuproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private String content;
    private int restaurantTableNo;
    private LocalDateTime regDate;
    private String division;
    private Long restaurantId;
}
