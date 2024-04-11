package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.dto.RestaurantDTO;
import kr.ganjuproject.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("join")
    public String restaurantPage(){
        return "manager/joinRestaurant";
    }

    @PostMapping("join")
    public String insertRestaurant(HttpServletRequest request, @RequestParam MultipartFile logo,
                                   RestaurantDTO restaurantDTO, Authentication authentication, Model model) throws IOException {
        if(authentication == null) return "home/home";

        Object principal = authentication.getPrincipal();

        Users user = ((PrincipalDetails) principal).getUser();

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setAddress(restaurantDTO.getAddressFirst() +" "+ restaurantDTO.getAddressElse());
        restaurant.setRestaurantTable(restaurantDTO.getRestaurantTable());

        restaurant.setUser(user);
        user.setRestaurant(restaurant);
        restaurantService.insertRestaurant(logo, restaurant);
        model.addAttribute("user",user);

        return "manager/joinSuccess";
    }

    @PutMapping("recognize/{keyId}")
    public @ResponseBody String recognizeRestaurant(@PathVariable Long keyId){
        Optional<Restaurant> optionalRestaurant = restaurantService.findById(keyId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();

            restaurant.setRecognize(1);
            restaurantService.save(restaurant);
        } else {
            return "no";
        }
        return "ok";
    }
}
