package kr.ganjuproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCategory {
    NOTICE("notice"),REPORT("report"),QUESTION("qustion");
    private final String role;
}
