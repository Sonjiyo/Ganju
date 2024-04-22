package kr.ganjuproject.controller;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.dto.OrderDTO;
import kr.ganjuproject.dto.OrderResponseDTO;
import kr.ganjuproject.dto.PaymentValidationRequest;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final RestaurantService restaurantService;
    private final OrdersService ordersService;
    private final MenuService menuService;
    private final MenuOptionService menuOptionService;
    private final MenuOptionValueService menuOptionValueService;
    private final RefundService refundService;
    private final PaymentService paymentService;

    // controller 에서 그냥 불러와서 써지네?
    private final SimpMessagingTemplate messagingTemplate;

    // 유저
    // 결재 하고 저장하는 부분
    @PostMapping("/user/validImpUid")
    public ResponseEntity<?> order(@RequestBody PaymentValidationRequest validationRequest, HttpSession session) {
        OrderResponseDTO dto = createOrders(validationRequest, session);

        // 정상적인 처리 응답을 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("order", dto);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
    // 유저
    // 비동기 환불처리
    @PostMapping("/user/validRefund")
    public ResponseEntity<?> refundOrder(@RequestBody Map<String, Object> payload) throws IOException {
        String reason = "테스트용"; // 환불 사유
        Long orderId = Long.valueOf((String) payload.get("orderId"));

        Orders order = ordersService.findById(orderId).get();

        System.out.println("order" + order);
        String accessToken = refundService.getAccessToken();
        Map<String, Object> refundResult = refundService.requestRefund(order, reason, accessToken);

        System.out.println("refundResult" + refundResult);
        System.out.println("order" + order);

        // "success" 키의 값이 true인지 확인하여 성공 여부를 판단
        Boolean success = (Boolean) refundResult.get("success");
        if (Boolean.TRUE.equals(success)) { // 성공 여부 확인
            return ResponseEntity.ok().body(Map.of("success", true, "message", (String)refundResult.get("message")));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", (String)refundResult.get("message")));
        }
    }

    // 유저
    // 모바일 결재 때문에 세션에 값 미리 넣어두려고 만든 비동기 어쩌구
    @PostMapping("/user/addValidSession")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentValidationRequest validationRequest, HttpSession session) {

        session.setAttribute("contents", validationRequest.getContents());
        session.setAttribute("totalPrice", validationRequest.getTotalPrice());
        // 클라이언트에게 성공 메시지를 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Session 저장 성공.");

        return ResponseEntity.ok(response); // HTTP 200 OK 상태 코드와 함께 응답 데이터 반환
    }

    // 유저
    @GetMapping("/user/payment/verify")
    public String  verifyPayment(@RequestParam("imp_uid") String impUid, HttpSession session) throws IOException {
        // 세션에서 저장된 데이터 가져오기
        int totalPrice = (int) session.getAttribute("totalPrice");
        log.info("impUid = " + impUid);

        // 결제 검증 로직 수행
        boolean isPaymentValid = paymentService.verifyPayment(impUid, totalPrice);

        log.info("isPaymentValid = " + isPaymentValid);
        if (isPaymentValid) {        // 주문 생성 메서드 호출

            // 결제 검증 성공 시, 주문 생성 로직 수행
            PaymentValidationRequest validationRequest = new PaymentValidationRequest();
            validationRequest.setImpUid(impUid);

            OrderResponseDTO orderResponse = createOrders(validationRequest, session);
            messagingTemplate.convertAndSend("/topic/calls", orderResponse);
            log.info("orderResponse = " + orderResponse);
            return "redirect:/user/order/" +orderResponse.getId();
        } else {
            log.info("검증실패");
            // 결제 검증 실패 시, 결제 실패 페이지로 리디렉션
            return "redirect:/user/main";
        }
    }


    // 중복된 함수를 여러번 돌려기 위해 만든
    public OrderResponseDTO createOrders(PaymentValidationRequest validationRequest, HttpSession session){
        // 세션에서 주문 정보 및 관련 정보 가져오기
        Long restaurantId = (Long)session.getAttribute("restaurantId");
        int restaurantTableNo = (int)session.getAttribute("restaurantTableNo");
        List<OrderDTO> ordersDTO = (List<OrderDTO>) session.getAttribute("orders");

        Orders newOrder = new Orders();
        newOrder.setRestaurantTableNo(restaurantTableNo);
        newOrder.setRestaurant(restaurantService.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found")));
        newOrder.setPrice((int)session.getAttribute("totalPrice"));
        newOrder.setRegDate(LocalDateTime.now());
        newOrder.setContent((String)session.getAttribute("contents"));
        newOrder.setUid(validationRequest.getImpUid());
        newOrder.setDivision(RoleOrders.WAIT);
        List<OrderMenu> orderMenus = new ArrayList<>();

        for (OrderDTO orderDTO : ordersDTO) {
            OrderMenu orderMenu = new OrderMenu();
            orderMenu.setMenuName(menuService.findById(orderDTO.getMenuId()).orElseThrow(() -> new RuntimeException("Menu not found")).getName());
            orderMenu.setQuantity(orderDTO.getQuantity());
            orderMenu.setPrice(menuService.findById(orderDTO.getMenuId()).get().getPrice()); // 기본 가격 설정
            orderMenu.setOrder(newOrder); // 연결된 주문 설정

            List<OrderOption> orderOptions = new ArrayList<>();
            for (OrderDTO.OptionSelection selectedOption : orderDTO.getSelectedOptions()) {
                OrderOption orderOption = new OrderOption();
                MenuOption menuOption = menuOptionService.findById(selectedOption.getOptionId());
                MenuOptionValue menuOptionValue = menuOptionValueService.findById(selectedOption.getValueId());
                orderOption.setOptionName(menuOption.getContent() + ": " + menuOptionValue.getContent());
                orderOption.setPrice(menuOptionValue.getPrice()); // 추가 가격 설정
                orderOption.setOrderMenu(orderMenu); // 연결된 주문 메뉴 설정
                orderOptions.add(orderOption); // 옵션 목록에 추가
            }
            orderMenu.setOrderOptions(orderOptions); // 주문 메뉴에 옵션 설정
            orderMenus.add(orderMenu); // 주문 메뉴 목록에 추가
        }

        newOrder.setOrderMenus(orderMenus); // 주문에 주문 메뉴 목록 설정
        Orders savedOrder = ordersService.save(newOrder); // 주문 저장
        OrderResponseDTO dto = ordersService.convertToOrderResponseDTO(savedOrder);

        return dto;
    }
}
