package kr.ganjuproject.dto;

import lombok.Data;

//결제 검증 요청 데이터 클래스
@Data
public class PaymentValidationRequest {
    private String impUid; // uid
    private String contents; // 요구사항
    private int totalPrice; // 총 금액
}
