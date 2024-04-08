package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("join")
    public String join(){

        return "manager/join";
    }

    @PostMapping("join")
    public @ResponseBody String insertUser(Users user){
        System.out.println(user);
        return "";
    }

    @PostMapping("join/{loginId}")
    public @ResponseBody String validIdCheck(@PathVariable String loginId){
        return managerService.isVaildId(loginId) ? "ok" : "no";
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
