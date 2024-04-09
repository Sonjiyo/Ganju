package kr.ganjuproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ganjuproject.dto.CategoryDTO;
import kr.ganjuproject.dto.MenuDTO;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.CategoryService;
import kr.ganjuproject.service.MenuService;
import kr.ganjuproject.service.ReviewService;
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
    private final CategoryService categoryService;
    private final ReviewService reviewService;

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
        Map<String, Object> response = new HashMap<>();  List<CategoryDTO> categories = categoryService.findCategoriesByRestaurantId(1L);
        List<MenuDTO> menus = menuService.findMenusByRestaurantId(1L);

        response.put("categories", categories);
        response.put("menus", menus);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public String info(@RequestParam Long id, Model model) {
        Optional<Menu> menu = menuService.findById(id);

        if (menu.isPresent()) {
            Menu m = menu.get();
            model.addAttribute("menu", m);
            return "user/info";
        } else {
            return "redirect:/user/main";
        }
    }

    @PostMapping("/info")
    public String info() {

        return "user/cart";
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
    public ResponseEntity<String> addMenu(@RequestBody String menu) {
        try {
            System.out.println("menu = " + menu);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> input = mapper.readValue(menu, new
                    TypeReference<Map<String, String>>() {
                    });
            Menu obj = new Menu();
            obj.setName(input.get("name"));
            obj.setPrice(Integer.parseInt(input.get("price")));
            Category test = categoryService.findByRestaurantId(1L).get(0);
            obj.setCategory(test);
            menuService.add(obj);
            return ResponseEntity.ok().body("메뉴가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 등록에 실패하였습니다.");
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
