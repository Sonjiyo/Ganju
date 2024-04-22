package kr.ganjuproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ganjuproject.auth.PrincipalDetails;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.service.CategoryService;
import kr.ganjuproject.service.MenuOptionService;
import kr.ganjuproject.service.MenuService;
import kr.ganjuproject.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final MenuService menuService;
    private final CategoryService categoryService;
    private final RestaurantService restaurantService;

    @GetMapping("/main")
    public String menuCategory(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();
            if (user.getLoginId().equals("admin")) return "redirect:/";

            List<Category> categories = categoryService.findByRestaurantId(user.getRestaurant().getId());
            categories.sort(Comparator.comparingInt(Category::getTurn));
            model.addAttribute("categories", categories);
            List<Menu> menus = menuService.findByRestaurantId(user.getRestaurant().getId());
            System.out.println("menus = " + menus);
            model.addAttribute("menus", menus);
        }
        return "manager/menuCategory";
    }

    @GetMapping("/add")
    public String addCategory(Model model) {
        List<Category> categories = categoryService.findByRestaurantId(1L);
        model.addAttribute("categories", categories);
        return "manager/menuCategory";
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addCategory(@RequestBody Map<String, String> category, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();

            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            Users user = principalDetails.getUser();

            Category cg = new Category();
            cg.setName(category.get("name"));
            Optional<Restaurant> restaurantOptional = restaurantService.findById(user.getRestaurant().getId());
            if (restaurantOptional.isPresent()) {
                cg.setRestaurant(restaurantOptional.get());
            } else {
                // Handle case when restaurant with ID 1 is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("레스토랑을 찾을 수 없습니다");
            }
            cg.setTurn(categoryService.findByRestaurantId(user.getRestaurant().getId()).size() + 1);
            categoryService.add(cg);
            return ResponseEntity.ok().body("카테고리 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카테고리 등록 실패 : " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            Optional<Category> categoryOptional = categoryService.findById(id);
            if (categoryOptional.isPresent()) {
                categoryService.delete(id);
                return ResponseEntity.ok().body("카테고리 삭제 성공");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카테고리를 찾을 수 없습니다");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카테고리 삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping(value = "/swapTurn")
    public ResponseEntity<Map<String, String>> swapTurn(@RequestBody Map<String, Object> requestBody) {
        Long id1 = Long.parseLong(requestBody.get("id1").toString());
        Long id2 = Long.parseLong(requestBody.get("id2").toString());
        if (id1 == null || id2 == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "ID가 없습니다");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            categoryService.swapTurn(id1, id2);
            Map<String, String> response = new HashMap<>();
            response.put("message", "턴이 성공적으로 변경되었습니다");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "턴 변경 중 오류가 발생했습니다");
            return ResponseEntity.status(500).body(response);
        }
    }
}
