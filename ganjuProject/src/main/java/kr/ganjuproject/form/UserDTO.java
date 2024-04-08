package kr.ganjuproject.form;

import lombok.Data;

@Data
public class UserDTO {
    private String loginId;
    private String password;
    private String phone;
    private String email;
}