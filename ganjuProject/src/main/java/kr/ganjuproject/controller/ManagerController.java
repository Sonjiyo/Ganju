package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.dto.UserDTO;
import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.ManagerService;
import kr.ganjuproject.service.OrdersService;
import kr.ganjuproject.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final BoardService boardService;
    private final OrdersService ordersService;
    private final ReviewService reviewService;

    @GetMapping("")
    public String main(Authentication authentication, Model model){
        if(authentication == null) return "redirect:/";

        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if(user.getLoginId().equals("admin")) return "redirect:/";

            double starAvg = reviewService.getAverageRating(user.getRestaurant().getId());
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime startTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MIN); // 오늘 날짜의 시작
            LocalDateTime endTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MAX); // 오늘 날짜의 끝

            List<Orders> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);

            Map<String, Object> map = ordersService.getRestaurantOrderData(list);
            model.addAttribute("user", user);
            model.addAttribute("orderCount", map.get("count"));
            model.addAttribute("orderPrice", map.get("price"));
            model.addAttribute("reportCount", boardService.getReortAcceptList(user.getRestaurant()));
            model.addAttribute("call", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.CALL).size());
            model.addAttribute("wait", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.WAIT).size());
            model.addAttribute("okay", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.OKAY).size());
            model.addAttribute("starAvg", starAvg);
        }

        return "manager/home";
    }

    @GetMapping("join")
    public String join(){return "manager/join";}

    @PostMapping("join")
    public @ResponseBody String insertUser(@RequestBody UserDTO userDTO) {
        Users user = new Users();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setRole(RoleUsers.ROLE_MANAGER);
        managerService.insertUser(user);

        return "";
    }

    @GetMapping("join/{loginId}")
    public @ResponseBody String validUsernameCheck(@PathVariable(value = "loginId") String loginId){
        return managerService.isVaildLoginId(loginId) ? "ok" : "no";
    }

    @DeleteMapping("/{keyId}")
    public @ResponseBody String DeleteManager(@PathVariable Long keyId){
        managerService.deleteUser(keyId);
        return "ok";
    }
}
