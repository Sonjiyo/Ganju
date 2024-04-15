package kr.ganjuproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleOrders {
    CALL("call"), WAIT("wait"), OKAY("okay");
    private final String role;
}
