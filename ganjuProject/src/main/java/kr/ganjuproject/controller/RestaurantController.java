package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.form.RestaurantDTO;
import kr.ganjuproject.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("join")
    public String insertRestaurant(HttpServletRequest request, @RequestParam MultipartFile logo,
                                   RestaurantDTO restaurantDTO, HttpSession session) throws IOException {
        Users user = (Users) session.getAttribute("log");
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setAddress(restaurantDTO.getAddressFirst()+restaurantDTO.getAddressElse());
        restaurant.setRestaurantTable(restaurantDTO.getRestaurantTable());

        restaurant.setUser(user);
        user.setRestaurant(restaurant);
        restaurantService.insertRestaurant(logo, restaurant);
        return "manager/joinSuccess";
    }
}
