package kr.ganjuproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleUsers {
    ROLE_USER("user"), ROLE_ADMIN("admin"), ROLE_MANAGER("manager");
    private final String role;
}
