package kr.ganjuproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String password;
    private String email;
    private String phone;
    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Restaurant restaurant;
    private int approve;
    @Enumerated(EnumType.STRING)
    private RoleUsers role;

    @Builder
    public Users(String loginId, String password, String email,String phone, String provider, String providerId) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.provider = provider;
        this.providerId = providerId;
        this.role=RoleUsers.ROLE_USER;
    }

    private String provider;
    private String providerId;
}
