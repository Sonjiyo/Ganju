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

    // 메인 메뉴 첫 페이지
    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
        Long restaurantId = 1L;
        int restaurantTableNo = 1;
        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(restaurantId);
        model.addAttribute("categories", categories);
        List<MenuDTO> menus = menuService.findMenusByRestaurantId(restaurantId);
        model.addAttribute("menus", menus);
//      리뷰 평균 점수
        model.addAttribute("staAve", reviewService.getAverageRating(restaurantId));
        session.setAttribute("restaurantId", restaurantId);
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

    // 장바구니 페이지 이동 시 session에 값이 있다면 가지고 감
    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderDTO order : orders) {
            OrderDetails orderDetails = ordersService.getOrderDetails(order);
            orderDetailsList.add(orderDetails);
        }

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

    // uid를 감추고 싶어 만든 비동기 저장
    @PostMapping("/validImpUid")
    public ResponseEntity<?> order(@RequestBody PaymentValidationRequest validationRequest, HttpSession session) {
        // 여기에서 imp_uid를 사용하여 결제 검증 로직을 구현하고,
        // 검증 성공 시 세션에서 주문 정보를 조회한 후 데이터베이스에 저장합니다.
        Long restaurantId = (Long)session.getAttribute("restaurantId");
        int restaurantTableNo = (int)session.getAttribute("restaurantTableNo");

        System.out.println("결재 성공시 들어오는 곳");
        System.out.println("validationRequest" + validationRequest);

        // 세션에서 주문 정보 가져오기 (이 예제에서는 session.getAttribute를 사용하여 구현)
        List<OrderDTO> orders = (List<OrderDTO>) session.getAttribute("orders");

        if (orders == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 정보가 없습니다.");
        }

        // 주문 정보를 데이터베이스에 저장하는 로직 구현 (생략)
        Orders order = new Orders();
        order.setRestaurantTableNo(restaurantTableNo);
        List<OrderMenu> orderMenuList = new ArrayList<>();
        for(OrderDTO o : orders){
            OrderMenu orderMenu = new OrderMenu();
            Menu menu = menuService.findById(o.getMenuId()).orElseThrow(() -> new RuntimeException("Menu not found"));
            int menuTotalPrice = menu.getPrice();
            StringBuilder menuString = new StringBuilder();
            menuString.append(menu.getName()).append(o.getQuantity()).append("개; ");
            menuString.append("가격:").append(menu.getPrice()).append("원; ");

            // 옵션 정보를 추가하는 부분
            menuString.append("options: ");
            for (OrderDTO.OptionSelection selectedOption : o.getSelectedOptions()) {
                MenuOption option = menuOptionService.findById(selectedOption.getOptionId());
                MenuOptionValue value = menuOptionValueService.findById(selectedOption.getValueId());

                menuString.append(option.getContent()).append(": ").append(value.getContent()).append(", ");
                menuTotalPrice += value.getPrice();
            }

            // 마지막 콤마 제거
            if (!o.getSelectedOptions().isEmpty()) {
                int lastCommaIndex = menuString.lastIndexOf(", ");
                menuString.delete(lastCommaIndex, menuString.length());
            }

            menuString.append("; total: ").append(menuTotalPrice * o.getQuantity()); // 옵션 가격을 포함한 총 가격 계산 필요
            orderMenu.setOrderMenu(menuString.toString());
            orderMenu.setQuantity(o.getQuantity());
            System.out.println(menuString);
            orderMenuList.add(orderMenu);
        }

        order.setOrderMenus(orderMenuList);
        order.setRestaurant(restaurantService.findById(restaurantId).get());
        order.setPrice(validationRequest.getTotalPrice());
        order.setRegDate(LocalDateTime.now());
        order.setContent(validationRequest.getContents());
        order.setUid(validationRequest.getImpUid());
        order.setDivision(RoleOrders.WAIT);

        ordersService.add(order);
        System.out.println(order);
        System.out.println("여기까지");
        return ResponseEntity.ok("결제 검증 및 주문 정보 저장 성공");
    }

    @GetMapping("/order")
    public String order() {

        return "user/order";
    }

    @GetMapping("/review")
    public String review() {
        return "user/review";
    }

    @PostMapping("/review")
    public String review(Model model) {
        return "redirect:/user/main";
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
}
