package kr.ganjuproject.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private Integer price;
    private String menuImage;
    private String info;
    // 메뉴가 속한 카테고리의 ID. 필요에 따라 카테고리 이름 등 추가 정보 포함 가능
    private Long categoryId;


    // 옵션 데이터를 위한 새 필드
    private List<String> optionsType = new ArrayList<>(); // 옵션 타입 필드 (필수/선택)
    private List<OptionDTO> options  = new ArrayList<>();; // 세부 옵션 데이터를 위한 리스트

    // getters and setters

    // OptionDTO는 세부 옵션을 나타내는 DTO
    @Data
    public static class OptionDTO {
        private String type; // 옵션 타입 코드 (REQUIRED/OPTIONAL)
        private String name; // 옵션 이름
        private List<DetailDTO> details; // 세부 옵션 목록

        // getters and setters

        // DetailDTO는 세부 옵션의 이름과 가격을 나타냄
        @Data
        public static class DetailDTO {
            private String name;
            private int price;

            // getters and setters
        }
    }
}