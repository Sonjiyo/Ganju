package kr.ganjuproject.controller;

import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @GetMapping("")
    public String orderPage(){

        return "manager/order";
    }

    @GetMapping("sales")
    public String sales(){
        return "manager/sales";
    }

}
