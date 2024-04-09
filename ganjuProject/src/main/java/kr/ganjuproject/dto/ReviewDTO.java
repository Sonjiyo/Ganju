package kr.ganjuproject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private String name;
    private String content;
    private Integer star;
    private LocalDateTime regDate;
    private int secret;
}
