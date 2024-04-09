package kr.ganjuproject.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    // 카테고리의 순서. 필요에 따라 추가적인 정보 포함 가능
    private Integer turn;
}
