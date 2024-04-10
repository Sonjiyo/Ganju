package kr.ganjuproject.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String loginId;
    private String password;
    private String phone;
    private String email;
}