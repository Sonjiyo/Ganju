package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.dto.OrderDetails;
import kr.ganjuproject.dto.OrderDTO;
import kr.ganjuproject.dto.OrderResponseDTO;
import kr.ganjuproject.dto.UpdateDate;
import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.RoleOrders;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.OrdersService;
import kr.ganjuproject.service.RestaurantService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;
    private final RestaurantService restaurantService;

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

            List<Orders> list = ordersService.getRestaurantOrdersWithinTime(user.getRestaurant(), startTime, currentTime);
            for(Orders order : list) {
                order.setReview(null);
                order.setRestaurant(null);
            }

            model.addAttribute("list", list);
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
            Map<String, Object> total = ordersService.getRestaurantOrderData(list);
            
            model.addAttribute("list", list);
            model.addAttribute("totalPrice", total.get("price"));
            model.addAttribute("totalCount", total.get("count"));
            return "manager/sales";
        }
        return "redirect:/";
    }

    @PostMapping("/sales")
    public ResponseEntity<Map<String, Object>> dateSales(Authentication authentication, @RequestBody UpdateDate date) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            response.put("status", "no");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if ("admin".equals(user.getLoginId())) {
                response.put("status", "no");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            LocalDateTime startTime = LocalDateTime.of(date.getStart().toLocalDate(), LocalTime.MIN); // 시작 날짜
            LocalDateTime endTime = LocalDateTime.of(date.getEnd().toLocalDate(), LocalTime.MAX); // 끝 날짜

            List<Orders> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);
            Map<String, Object> total = ordersService.getRestaurantOrderData(list);

            // 응답에 필요한 데이터를 status와 함께 묶어서 전달
            response.put("status", "ok");
            response.put("list", list);
            response.put("totalPrice", total.get("price"));
            response.put("totalCount", total.get("count"));

            return ResponseEntity.ok(response);
        }

        response.put("status", "no");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @DeleteMapping("/order/{keyId}")
    public @ResponseBody String deleteOrder(@PathVariable Long keyId){
        ordersService.deleteOrder(keyId);
        return "ok";
    }

    @PutMapping("/order/{keyId}")
    public @ResponseBody String recognizeOrder(@PathVariable Long keyId){
        Orders order = ordersService.recognizeOrder(keyId);
        return order ==null ? "no" : "ok";
    }

    // 호출하기에서 값을 가져와서 orders에 저장하고 다시 orders로 내보냄
    @PostMapping("/validUserCall")
    public ResponseEntity<?> save(HttpSession session, @RequestBody String content){
        long restaurantId = (long) session.getAttribute("restaurantId");
        int restaurantTableNo = (int) session.getAttribute("restaurantTableNo");
        Orders order = new Orders();
        order.setRestaurantTableNo(restaurantTableNo);
        order.setPrice(0);
        order.setRegDate(LocalDateTime.now());
        order.setRestaurant(restaurantService.findById(restaurantId).get());
        order.setContent(content);
        order.setDivision(RoleOrders.CALL);

        Orders saveOrder = ordersService.save(order);

        // Orders 엔티티를 OrderResponseDTO로 변환
        OrderResponseDTO orderResponseDTO = ordersService.convertToOrderResponseDTO(saveOrder);
        System.out.println(orderResponseDTO);
        System.out.println("저장성공");
        return ResponseEntity.ok().body(Map.of("order", orderResponseDTO));
    }
}
