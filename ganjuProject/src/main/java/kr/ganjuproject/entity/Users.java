package kr.ganjuproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
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
    @JsonManagedReference
    private Restaurant restaurant;
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
    @Column(nullable = true)
    private String provider;
    @Column(nullable = true)
    private String providerId;
}
