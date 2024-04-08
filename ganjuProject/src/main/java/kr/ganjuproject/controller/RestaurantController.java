package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("join")
    public String insertRestaurant(@RequestBody Restaurant restaurant, HttpSession session){
        Users user = (Users) session.getAttribute("log");
        restaurant.setUser(user);
        user.setRestaurant(restaurant);
        restaurantService.insertRestaurant(restaurant);
        return "manager/joinSuccess";
    }
}
