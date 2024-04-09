package kr.ganjuproject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {
    private Long id;
    private String name;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private String boardCategory; // Enum 타입을 String으로 변환
}
