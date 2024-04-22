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
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant/join")
    public String insertRestaurant(HttpServletRequest request, @RequestParam MultipartFile logo,
                                   RestaurantDTO restaurantDTO, Authentication authentication, Model model) throws IOException {
        if(authentication == null) return "home/home";

        Object principal = authentication.getPrincipal();

        Users user = ((PrincipalDetails) principal).getUser();

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setAddress(restaurantDTO.getAddressFirst() +"/"+ restaurantDTO.getAddressElse());
        restaurant.setRestaurantTable(restaurantDTO.getRestaurantTable());

        restaurant.setUser(user);
        user.setRestaurant(restaurant);
        restaurantService.insertRestaurant(logo, restaurant);
        model.addAttribute("user",user);

        return "manager/joinSuccess";
    }

    @PutMapping("/restaurant/recognize/{keyId}")
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

    @DeleteMapping("/restaurant/{keyId}")
    public @ResponseBody String deleteRestaurant(@PathVariable Long keyId){
        Optional<Restaurant> optionalRestaurant = restaurantService.findById(keyId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            restaurantService.delete(restaurant);
            return "ok";
        }
        return "no";
    }

    @PostMapping("/restaurant/update")
    public String updateRestaurant(HttpServletRequest request, @RequestParam MultipartFile logo,
                                   RestaurantDTO restaurantDTO, Authentication authentication, Model model)throws IOException{
        Object principal = authentication.getPrincipal();

        Users user = ((PrincipalDetails) principal).getUser();

        Restaurant restaurant = user.getRestaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setAddress(restaurantDTO.getAddressFirst() +"/"+ restaurantDTO.getAddressElse());
        restaurant.setRestaurantTable(restaurantDTO.getRestaurantTable());

        restaurantService.deleteLogo(restaurant.getLogo());
        restaurantService.insertRestaurant(logo, restaurant);
        return "redirect:/manager/myPage";
    }
}
