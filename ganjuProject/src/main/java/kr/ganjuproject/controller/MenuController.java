package kr.ganjuproject.controller;

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
import org.springframework.web.multipart.MultipartFile;

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

    // 헤더 부분 값 념거주는 메서드
    public Map<String, Object> someMethod(boolean showIcon, String name, boolean showBasket) {
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
        if (!menuOptions.isEmpty()) {
            model.addAttribute("menuOptions", menuOptions);
            for (int i = 0; i < menuOptions.size(); i++) {
                List<MenuOptionValue> menuOptionValues = menuOptionValueService.findByMenuOptionId(menuOptions.get(i).getId());
                menuOptionValueMap.put(menuOptions.get(i).getId().toString(), menuOptionValues);
            }
        }
        // 헤더 부분
        model.addAttribute("headerArgs", someMethod(true, "", true));

        if (menu.isPresent()) {
            Menu m = menu.get();
            model.addAttribute("menu", m);
            if (!menuOptionValueMap.isEmpty()) {
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

    @PostMapping("/cart")
    public String cart() {

        return "user/order";
    }

    // 주문완료하고 시킨메뉴 보여주는 곳
    @GetMapping("/order/{orderId}")
    public String order(@PathVariable("orderId") Long orderId, Model model, HttpSession session) {
        Optional<Orders> order = ordersService.findById(orderId);

        if (order.isPresent()) {
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

        Restaurant restaurant = restaurantService.findById((Long) session.getAttribute("restaurantId")).get();
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

    @GetMapping("/editMenu/{id}")
    public String editMenu(Model model, @PathVariable Long id) {
        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(1L);
        model.addAttribute("categories", categories);
        Menu menus = menuService.findById(id).orElse(null);
        model.addAttribute("menus", menus);
        return "manager/editMenu";
    }

    @PostMapping("/addMenu")
    public String addMenu(@RequestParam MultipartFile image, MenuDTO menuDTO, Model model) throws IOException {
        Menu menu = new Menu();
        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        menu.setInfo(menuDTO.getInfo());
        Category category = (categoryService.findById(menuDTO.getCategoryId()).orElse(null));
        menu.setCategory(category);
        String storedFileName = menuService.uploadImage(image);
        menu.setMenuImage(storedFileName);
        menuService.addMenu(image, menu);
        model.addAttribute("menu", menu);
        System.out.println("category = " + category);
        System.out.println("menu = " + menu);
        return "manager/menuCategory";

//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, String> input = mapper.readValue(menuData, new TypeReference<Map<String, String>>() {});
//            // 받아온 레스토랑 ID를 사용하여 해당 레스토랑에 속한 카테고리를 데이터베이스에서 조회
//            Long restaurantId = Long.parseLong(input.get("restaurantId"));
//            Long categoryId = Long.parseLong(input.get("categoryId"));
//            // 여기서 적절한 카테고리 선택 로직이 필요합니다. 예를 들어 첫 번째 카테고리를 선택하거나, 특정 조건에 따라 선택할 수 있습니다.
//            Category category = categoryService.findById(categoryId)
//                    .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 카테고리를 찾을 수 없습니다"));
//            Menu obj = new Menu();
//            obj.setName(input.get("name"));
//            obj.setPrice(Integer.parseInt(input.get("price")));
//            obj.setCategory(category); // 조회된 카테고리를 메뉴 객체에 설정
//            obj.setInfo(input.get("info"));
//            obj.setMenuImage(input.get("image"));
//            obj.setRestaurant(restaurantService.findById(restaurantId)
//                    .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 레스토랑을 찾을 수 없습니다")));
//            menuService.add(obj, image);
//            return ResponseEntity.ok().body("메뉴가 성공적으로 등록 되었습니다");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 등록에 실패하였습니다");
//        }
    }

    @PostMapping("/updateMenu/{id}")
    public String updateMenu(@RequestBody Map<String, String> requestBody, @PathVariable Long id) {
        String newName = requestBody.get("name");
        String newPrice = requestBody.get("price");
        String newInfo = requestBody.get("info");
        Menu menu = menuService.getOneMenu(id);
        menu.setName(newName);
        menu.setPrice(Integer.parseInt(newPrice));
        menu.setInfo(newInfo);
        menuService.updateMenu(menu);
        return "redirect:/category/main";
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
