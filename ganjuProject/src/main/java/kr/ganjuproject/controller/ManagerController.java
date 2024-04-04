package kr.ganjuproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    @GetMapping("join")
    public String join(){
        return "manager/join";
    }
    @GetMapping("joinVerification")
    public String joinVerification(){
        return "manager/joinVerification";
    }

    @GetMapping("joinRestaurant")
    public String joinRestaurant(){
        return "manager/joinRestaurant";
    }
    @GetMapping("joinSuccess")
    public String joinSuccess(){
        return "manager/joinSuccess";
    }

    @GetMapping("order")
    public String order(){
        return "manager/order";
    }
    @GetMapping("sales")
    public String salesr(){
        return "manager/sales";
    }
}
