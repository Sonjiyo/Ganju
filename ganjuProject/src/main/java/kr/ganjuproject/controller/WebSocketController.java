package kr.ganjuproject.controller;

import kr.ganjuproject.dto.OrderResponseDTO;
import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final OrdersService ordersService;

    @MessageMapping("/call")
    @SendTo("/topic/calls")
    public OrderResponseDTO call(OrderResponseDTO order) throws Exception {
        // 처리 로직
        return order;
    }
}
