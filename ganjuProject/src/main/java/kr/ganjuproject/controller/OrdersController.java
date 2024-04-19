package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.dto.*;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.service.*;
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

import java.io.IOException;
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

            List<OrderResponseDTO> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);
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

            List<OrderResponseDTO> list = ordersService.getRestaurantOrdersWithinTimeWithoutCall(user.getRestaurant(), startTime, endTime);
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

    // cart에서 수량 변경 시 비동기로 업데이트
    @PostMapping("/updateValidQuantity")
    public ResponseEntity<?> updateMenuQuantity(HttpSession session, @RequestBody OrderDTO orderDTO){

        // 세션에서 주문 목록을 가져옴
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");
        if (orders == null) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        // 메뉴 ID와 일치하는 주문 찾기 및 수량 업데이트
        for (OrderDTO order : orders) {
            System.out.println("order.getMenuId() = " + order.getMenuId());
            System.out.println("MenuID = " + orderDTO.getMenuId());
            System.out.println("Quantity = " + orderDTO.getMenuId());
            if (order.getMenuId() == orderDTO.getMenuId()) {
                order.setQuantity(orderDTO.getQuantity()); // 수량 업데이트
                break; // 메뉴 ID가 일치하는 첫 번째 주문만 업데이트
            }
        }

        // 업데이트된 주문 목록을 세션에 저장
        session.setAttribute("orders", orders);
        System.out.println(orders);
        // 정상적인 처리 응답을 JSON 형태로 반환
        Map<String, String> response = new HashMap<>();
        response.put("data", "수량이 업데이트되었습니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/removeValidOrder")
    public ResponseEntity<?> removeValidOrder(HttpSession session, @RequestParam Long menuId) {
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        // 메뉴 ID와 일치하는 주문을 찾아서 삭제
        boolean removed = orders.removeIf(order -> order.getMenuId().equals(menuId));
        if (!removed) {
            return ResponseEntity.badRequest().body("해당 메뉴 ID를 가진 주문이 장바구니에 없습니다.");
        }

        // 삭제 후 주문 목록이 비어 있으면 세션에서 orders 속성 삭제
        if (orders.isEmpty()) {
            session.removeAttribute("orders");
            Map<String, Object> response = new HashMap<>();
            response.put("message", "장바구니가 비워졌습니다.");
            return ResponseEntity.ok(response);
        } else {
            // 아니면 업데이트된 주문 목록을 세션에 저장
            session.setAttribute("orders", orders);

            // 정상적인 처리 응답을 JSON 형태로 반환
            Map<String, Object> response = new HashMap<>();
            response.put("message", "주문이 삭제되었습니다.");
            response.put("orders", orders);
            return ResponseEntity.ok(response);
        }
    }

    // 리뷰를 쓰려고 할 때 식당 측에서 주문 거부나 환불을 시켰다면 리뷰 거부
    @PostMapping("/validOrderCheck")
    public ResponseEntity<?> validOrderCheck(HttpSession session, @RequestParam Long menuId) {
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        // 메뉴 ID와 일치하는 주문을 찾아서 삭제
        boolean removed = orders.removeIf(order -> order.getMenuId().equals(menuId));
        if (!removed) {
            return ResponseEntity.badRequest().body("해당 메뉴 ID를 가진 주문이 장바구니에 없습니다.");
        }

        // 삭제 후 주문 목록이 비어 있으면 세션에서 orders 속성 삭제
        if (orders.isEmpty()) {
            session.removeAttribute("orders");
            Map<String, Object> response = new HashMap<>();
            response.put("message", "장바구니가 비워졌습니다.");
            return ResponseEntity.ok(response);
        } else {
            // 아니면 업데이트된 주문 목록을 세션에 저장
            session.setAttribute("orders", orders);

            // 정상적인 처리 응답을 JSON 형태로 반환
            Map<String, Object> response = new HashMap<>();
            response.put("message", "주문이 삭제되었습니다.");
            response.put("orders", orders);
            return ResponseEntity.ok(response);
        }
    }

    // 비동기로 orders 에 값이 있는지 확인
}
