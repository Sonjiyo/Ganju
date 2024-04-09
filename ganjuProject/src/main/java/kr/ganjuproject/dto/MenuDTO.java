package kr.ganjuproject.dto;

import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private Integer price;
    private String menuImage;
    private String info;
    // 메뉴가 속한 카테고리의 ID. 필요에 따라 카테고리 이름 등 추가 정보 포함 가능
    private Long categoryId;
}