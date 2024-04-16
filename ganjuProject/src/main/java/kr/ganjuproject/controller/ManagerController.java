package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@Slf4j
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final ManagerService managerService;
    private final BoardService boardService;
    private final OrdersService ordersService;
    private final ReviewService reviewService;

    private static final String PHONE_REGEX = "^\\d{3}-\\d{4}-\\d{4}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    @GetMapping("")
    public String main(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";

            double starAvg = reviewService.getAverageRating(user.getRestaurant().getId());
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime startTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MIN); // 오늘 날짜의 시작
            LocalDateTime endTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MAX); // 오늘 날짜의 끝

            List<Orders> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);

            Map<String, Object> total = ordersService.getRestaurantOrderData(list);
            model.addAttribute("user", user);
            model.addAttribute("orderCount", total.get("count"));
            model.addAttribute("orderPrice", total.get("price"));
            model.addAttribute("reportCount", boardService.getReortAcceptList(user.getRestaurant()));
            model.addAttribute("call", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.CALL).size());
            model.addAttribute("wait", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.WAIT).size());
            model.addAttribute("okay", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.OKAY).size());
            model.addAttribute("starAvg", starAvg);
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
    public String update(@RequestBody Map<String, String> requestBody, Authentication authentication) {
        String inputPassword = requestBody.get("password");
        String inputPhone = requestBody.get("phone");
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();
        PrincipalDetails principalDetails = (PrincipalDetails) principal;
        Users user = principalDetails.getUser();
        // 입력된 비밀번호와 시큐리티로 해싱된 비밀번호를 비교
        if (passwordEncoder.matches(inputPassword, user.getPassword())) {
            // 비밀번호가 일치할 경우에만 전화번호 수정
            if (inputPhone != null && PHONE_PATTERN.matcher(inputPhone).matches()) {
                user.setPhone(inputPhone);
                managerService.updateUser(user);
                System.out.println("user = " + user);
                return "redirect:/manager/myPage";
            } else {
                throw new IllegalArgumentException("올바른 전화번호 형식이 아닙니다.");
            }
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @DeleteMapping("/{keyId}")
    public @ResponseBody String DeleteManager(@PathVariable Long keyId) {
        managerService.deleteUser(keyId);
        return "ok";

    }

}
