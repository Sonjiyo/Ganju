package kr.ganjuproject.controller;

import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @GetMapping("")
    public String orderPage(Authentication authentication, Model model){
        if(authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if(user.getLoginId().equals("admin")) return "redirect:/";

            model.addAttribute("list", ordersService.getRestaurantOrders(user.getRestaurant()));
            return "manager/order";
        }
        return "redirect:/";
    }

    @GetMapping("sales")
    public String sales(Authentication authentication, Model model){
        if(authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if(principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if(user.getLoginId().equals("admin")) return "redirect:/";

            Map<String, Object> map = ordersService.getRestaurantOrderData(user.getRestaurant());
            model.addAttribute("user", user);
            model.addAttribute("orderCount", map.get("count"));
            model.addAttribute("orderPrice", map.get("price"));
            return "manager/sales";
        }
        return "redirect:/";
    }

}
