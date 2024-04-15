package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.RoleOrders;
import kr.ganjuproject.entity.RoleUsers;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.dto.UserDTO;
import kr.ganjuproject.service.BoardService;
import kr.ganjuproject.service.ManagerService;
import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ManagerService managerService;
    private final BoardService boardService;
    private final OrdersService ordersService;

    @GetMapping("")
    public String main(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";

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
        }

        return "manager/home";
    }

    @GetMapping("join")
    public String join() {
        return "manager/join";
    }

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
    public @ResponseBody String validUsernameCheck(@PathVariable(value = "loginId") String loginId) {
        return managerService.isVaildLoginId(loginId) ? "ok" : "no";
    }

    @GetMapping("myPage")
    public String myPage(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";
            model.addAttribute("user", user);
        }
        return "/manager/myPage";
    }

    @GetMapping("myPageEdit")
    public String myPageEdit(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";
            model.addAttribute("user", user);
        }
        return "/manager/myPageEdit";
    }

    @PostMapping("/update")
    public String update(@RequestBody Map<String, String> requestBody, HttpSession session) {
        String inputPassword = requestBody.get("password");
        Users user = (Users) session.getAttribute("user");
        // 여기서 사용자 비밀번호가 맞는지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (passwordEncoder.matches(inputPassword, userDetails.getPassword())) {
                // 비밀번호가 일치할 경우에만 전화번호 수정
                String phone = requestBody.get("phone");
                if (phone != null && !phone.isEmpty()) {
                    user.setPhone(phone);
                }
                return "/manager/myPage";
            }
        }
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
    }
}
