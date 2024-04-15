package kr.ganjuproject.controller;

import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.RoleOrders;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @GetMapping("/orders")
    public String orderPage(Authentication authentication, Model model){
        if(authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if(user.getLoginId().equals("admin")) return "redirect:/";

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime startTime = currentTime.minusHours(24); // 현재시간으로부터 24시간 전까지

            model.addAttribute("list", ordersService.getRestaurantOrdersWithinTime(user.getRestaurant(), startTime, currentTime));
            model.addAttribute("callCount", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.CALL).size());
            model.addAttribute("waitCount", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.WAIT).size());
            model.addAttribute("okayCount", ordersService.getRestaurantOrdersDivision(user.getRestaurant(), RoleOrders.OKAY).size());
            return "manager/order";
        }
        return "redirect:/";
    }

    @GetMapping("/sales")
    public String sales(Authentication authentication, Model model){
        if(authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if(user.getLoginId().equals("admin")) return "redirect:/";

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime startTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MIN); // 오늘 날짜의 시작
            LocalDateTime endTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.MAX); // 오늘 날짜의 끝

            List<Orders> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);

            Map<String, Object> map = ordersService.getRestaurantOrderData(list);
            model.addAttribute("orderCount", map.get("count"));
            model.addAttribute("orderPrice", map.get("price"));
            model.addAttribute("list", list);
            return "manager/sales";
        }
        return "redirect:/";
    }

    @PostMapping("/sales")
    public @ResponseBody Map<String, Object> dateSales(Authentication authentication, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            response.put("status", "no");
            return response;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if ("admin".equals(user.getLoginId())) {
                response.put("status", "no");
                return response;
            }

            List<Orders> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);
            Map<String, Object> orderData = ordersService.getRestaurantOrderData(list);

            // 응답에 count와 price, list 값을 추가
            response.put("status", "ok");
            response.put("orderCount", orderData.get("count"));
            response.put("orderPrice", orderData.get("price"));
            response.put("orderList", list);

            return response;
        }

        response.put("status", "no");
        return response;
    }

}
