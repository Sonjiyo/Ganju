package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.entity.RoleUsers;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.dto.UserDTO;
import kr.ganjuproject.service.ManagerService;
import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final OrdersService ordersService;

    @GetMapping("")
    public String main(HttpSession session, Model model){
        if(session.getAttribute("log")==null
                || ((Users) session.getAttribute("log")).getLoginId().equals("admin")) return "redirect:/";
        Users user = (Users) session.getAttribute("log");
        Map<String, Object> map = ordersService.getRestaurantOrderData(user.getRestaurant());
        model.addAttribute("orderCount", map.get("count"));
        model.addAttribute("orderPrice", map.get("price"));


        return "manager/home";
    }

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
    public @ResponseBody String validUsernameCheck(@PathVariable(value = "loginId") String loginId){
        return managerService.isVaildLoginId(loginId) ? "ok" : "no";
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("joinRestaurant")
    public String joinRestaurant(){
        return "manager/joinRestaurant";
    }

    @GetMapping("joinSuccess")
    public String joinSuccess(){
        return "manager/joinSuccess";
    }


}
