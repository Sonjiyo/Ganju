package kr.ganjuproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleMenuOption {
    REQUIRED("required"), OPTIONAL("optional");
    private final String role;
}
