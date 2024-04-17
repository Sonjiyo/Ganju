package kr.ganjuproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.dto.*;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    private final RestaurantService restaurantService;
    private final MenuOptionService menuOptionService;
    private final MenuOptionValueService menuOptionValueService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final OrdersService ordersService;
    private final RefundService refundService;

    // 헤더 부분 값 념거주는 메서드
    public Map<String, Object> someMethod(boolean showIcon, String name, boolean showBasket){
        Map<String, Object> headerArgs = new HashMap<>();
        headerArgs.put("showIcon", showIcon);
        headerArgs.put("name", name);
        headerArgs.put("showBasket", showBasket);

        return headerArgs;
    }

    // 메인 메뉴 첫 페이지
    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
        Long restaurantId = 1L;
        int restaurantTableNo = 1;
        Restaurant restaurant = restaurantService.findById(restaurantId).get();

        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(restaurantId);
        model.addAttribute("categories", categories);
        List<MenuDTO> menus = menuService.findMenusByRestaurantId(restaurantId);
        model.addAttribute("menus", menus);
        // 헤더 부분
        model.addAttribute("headerArgs", someMethod(true, "", true));
//      리뷰 평균 점수
        model.addAttribute("staAve", reviewService.getAverageRating(restaurantId));
        session.setAttribute("restaurantId", restaurantId);
        session.setAttribute("restaurantName", restaurant.getName());
        session.setAttribute("restaurantTableNo", restaurantTableNo);
        return "user/main";
    }

    // 비동기 메인 메뉴 데이터
    @GetMapping("/validateMenuMenu")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validateMenu(Model model) {
        System.out.println("비동기 메뉴");
        Map<String, Object> response = new HashMap<>();
        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(1L);
        List<MenuDTO> menus = menuService.findMenusByRestaurantId(1L);

        response.put("categories", categories);
        response.put("menus", menus);

        return ResponseEntity.ok(response);
    }

    // 메뉴를 선택 했을 때 보여주는 창
    @GetMapping("/info")
    public String info(@RequestParam Long id, Model model) {
        Optional<Menu> menu = menuService.findById(id);
        // 일단 메뉴 id 값으로 메뉴 옵션을 불러오고
        List<MenuOption> menuOptions = menuOptionService.findByMenuId(menu.get().getId());
        Map<String, Object> menuOptionValueMap = new HashMap<>();
        // 메뉴 옵션이 없는 경우도 있으니 확인하고 비어 있지 않으면
        if(!menuOptions.isEmpty()){
            model.addAttribute("menuOptions", menuOptions);
            for(int i=0 ; i<menuOptions.size() ; i++){
                List<MenuOptionValue> menuOptionValues = menuOptionValueService.findByMenuOptionId(menuOptions.get(i).getId());
                menuOptionValueMap.put(menuOptions.get(i).getId().toString() , menuOptionValues);
            }
        }
        // 헤더 부분
        model.addAttribute("headerArgs", someMethod(true, "", true));

        if (menu.isPresent()) {
            Menu m = menu.get();
            model.addAttribute("menu", m);
            if(!menuOptionValueMap.isEmpty()){
                model.addAttribute("menuOptionValues", menuOptionValueMap);
            }
            return "user/info";
        } else {
            return "redirect:/user/main";
        }
    }

    // 메뉴 상세 정보 창
    @PostMapping("/info")
    public ResponseEntity<?> info(@RequestBody OrderDTO orderDTO, HttpSession session) {

        System.out.println(orderDTO);
        // 세션에서 주문 리스트를 가져옴. 없으면 새 리스트를 생성.
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");
        if (orders == null) {
            orders = new ArrayList<>();
        }

        // 현재 주문을 리스트에 추가
        orders.add(orderDTO);

        // 주문 리스트를 세션에 다시 저장
        session.setAttribute("orders", orders);

        // 정상적인 처리 응답을 JSON 형태로 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "주문이 성공적으로 처리되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        // 세션에서 orders를 가져올 때 null 체크를 하고, null일 경우 빈 리스트를 생성합니다.
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");
        if (orders == null) {
            orders = new ArrayList<>(); // orders가 null일 경우 빈 리스트를 생성합니다.
        }
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderDTO order : orders) {
            OrderDetails orderDetails = ordersService.getOrderDetails(order);
            orderDetailsList.add(orderDetails);
        }

        // 헤더 부분
        model.addAttribute("headerArgs", someMethod(true, "장바구니", false));
        model.addAttribute("orderDetailsList", orderDetailsList);

        return "user/cart"; // 장바구니 페이지로 이동
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
    @PostMapping("/cart")
    public String cart() {

        return "user/order";
    }

    @PostMapping("/validImpUid")
    public ResponseEntity<?> order(@RequestBody PaymentValidationRequest validationRequest, HttpSession session) {
        // 세션에서 주문 정보 및 관련 정보 가져오기
        Long restaurantId = (Long)session.getAttribute("restaurantId");
        int restaurantTableNo = (int)session.getAttribute("restaurantTableNo");
        List<OrderDTO> ordersDTO = (List<OrderDTO>) session.getAttribute("orders");

        if (ordersDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 정보가 없습니다.");
        }

        Orders newOrder = new Orders();
        newOrder.setRestaurantTableNo(restaurantTableNo);
        newOrder.setRestaurant(restaurantService.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found")));
        newOrder.setPrice(validationRequest.getTotalPrice());
        newOrder.setRegDate(LocalDateTime.now());
        newOrder.setContent(validationRequest.getContents());
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

        // 정상적인 처리 응답을 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("order", dto);
        System.out.println(dto);
        return ResponseEntity.ok(response);
    }

    // 주문완료하고 시킨메뉴 보여주는 곳
    @GetMapping("/order/{orderId}")
    public String order(@PathVariable("orderId") Long orderId, Model model, HttpSession session) {
        Optional<Orders> order = ordersService.findById(orderId);

        if(order.isPresent()){
            model.addAttribute("order", order.get());
        }
        // 헤더 부분
        model.addAttribute("headerArgs", someMethod(true, "주문완료", false));
        System.out.println(order);
        session.removeAttribute("orders");
        session.removeAttribute("orderDetailsList");
        return "user/order";
    }

    @GetMapping("/review/{orderId}")
    public String review(@PathVariable("orderId") Long orderId, Model model) {
        // 헤더 부분
        model.addAttribute("headerArgs", someMethod(true, "리뷰작성", false));
        model.addAttribute("orderId", orderId);
        return "user/review";
    }

    @PostMapping("/review")
    public String review(ReviewDTO reviewDTO, HttpSession session) {
        Review review = new Review();

        Restaurant restaurant = restaurantService.findById((Long)session.getAttribute("restaurantId")).get();
        review.setRestaurant(restaurant);
        review.setName(reviewDTO.getName());
        review.setContent(reviewDTO.getContent());
        review.setRegDate(LocalDateTime.now());
        review.setStar(reviewDTO.getStar());
        Orders order = ordersService.findById(reviewDTO.getOrderId()).get();
        review.setOrder(order);
        review.setSecret(0);

        reviewService.save(review);
        return "redirect:/menu/main";
    }

    @GetMapping("/add")
    public String addMenuForm(Model model) {
        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(1L);
        model.addAttribute("categories", categories);
        List<MenuDTO> menus = menuService.findMenusByRestaurantId(1L);
        model.addAttribute("menus", menus);
        return "manager/addMenu";
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addMenu(@RequestBody String menuData) {
        try {
            System.out.println("menu = " + menuData);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> input = mapper.readValue(menuData, new TypeReference<Map<String, String>>() {});
            // 받아온 레스토랑 ID를 사용하여 해당 레스토랑에 속한 카테고리를 데이터베이스에서 조회
            Long restaurantId = Long.parseLong(input.get("restaurantId"));
            Long categoryId = Long.parseLong(input.get("categoryId"));
            // 여기서 적절한 카테고리 선택 로직이 필요합니다. 예를 들어 첫 번째 카테고리를 선택하거나, 특정 조건에 따라 선택할 수 있습니다.
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 카테고리를 찾을 수 없습니다"));
            Menu obj = new Menu();
            obj.setName(input.get("name"));
            obj.setPrice(Integer.parseInt(input.get("price")));
            obj.setCategory(category); // 조회된 카테고리를 메뉴 객체에 설정
            obj.setRestaurant(restaurantService.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 레스토랑을 찾을 수 없습니다")));
            System.out.println("obj = " + obj);
            menuService.add(obj);
            return ResponseEntity.ok().body("메뉴가 성공적으로 등록 되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 등록에 실패하였습니다");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
        try {
            menuService.delete(id);
            return ResponseEntity.ok().body("메뉴(ID : " + id + ")가 삭제되었습니다");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID에 해당하는 메뉴를 찾을 수 없습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제 중 오류가 발생했습니다 : " + e.getMessage());
        }
    }

    // 비동기 환불처리
    @PostMapping("/refund")
    public ResponseEntity<?> refundOrder(@RequestBody Map<String, Object> payload) throws IOException {
        String reason = "테스트용"; // 환불 사유
        Long orderId = Long.valueOf((String) payload.get("orderId"));

        Orders order = ordersService.findById(orderId).get();

        System.out.println("order" + order);
        String accessToken = refundService.getAccessToken();
        Map<String, Object> refundResult = refundService.requestRefund(order.getUid(), reason, accessToken);

        System.out.println("refundResult" + refundResult);
        System.out.println("order" + order);
        // "success" 대신 "code" 키의 값이 0인지 확인하여 성공 여부를 판단
        Object codeObj = refundResult.get("code");
        if (codeObj != null && "0".equals(codeObj.toString())) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "환불 처리가 완료되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "환불 처리에 실패했습니다."));
        }
    }
}
