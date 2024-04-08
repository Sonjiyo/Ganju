package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.entity.RoleUsers;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.form.UserDTO;
import kr.ganjuproject.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("join")
    public String join(){return "manager/join";}

    @PostMapping("join")
    public @ResponseBody String insertUser(@RequestBody UserDTO userDTO, HttpSession session) {
        Users user = new Users();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setRole(RoleUsers.ROLE_MANAGER);
        managerService.insertUser(user);

        session.setAttribute("log", user);
        return "";
    }

    @GetMapping("join/{loginId}")
    public @ResponseBody String validIdCheck(@PathVariable String loginId){
        return managerService.isVaildId(loginId) ? "ok" : "no";
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
