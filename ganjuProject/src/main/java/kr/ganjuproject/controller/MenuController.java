package kr.ganjuproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.dto.CategoryDTO;
import kr.ganjuproject.dto.MenuDTO;
import kr.ganjuproject.dto.OrderDTO;
import kr.ganjuproject.dto.OrderDetails;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.entity.MenuOption;
import kr.ganjuproject.entity.MenuOptionValue;
import kr.ganjuproject.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String main(Model model) {
        List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(1L);
        model.addAttribute("categories", categories);
        List<MenuDTO> menus = menuService.findMenusByRestaurantId(1L);
        model.addAttribute("menus", menus);
//      리뷰 평균 점수
        model.addAttribute("staAve", reviewService.getAverageRating(1L));
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
    @PostMapping("/cart")
    public String cart() {

        return "user/order";
    }

    @PostMapping("/order")
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
