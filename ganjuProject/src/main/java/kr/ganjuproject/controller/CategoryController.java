package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.CategoryService;
import kr.ganjuproject.service.MenuOptionService;
import kr.ganjuproject.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final MenuOptionService menuOptionService;
    private final MenuService menuService;
    private final CategoryService categoryService;

    @GetMapping("/main")
    public String menuCategory(Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);
        List<Menu> menus = menuService.getList();
        System.out.println("menus = " + menus);
        System.out.println("menus = " + menus.size());
        model.addAttribute("menus", menus);
        return "manager/menuCategory";
    }

    @GetMapping("/add")
    public String addCategory(Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);
        return "manager/menuCategory";
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        try {
            categoryService.add(category);
            return ResponseEntity.ok().body("카테고리 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카테고리 등록 실패");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.ok().body("카테고리 (ID : " + id + ")가 삭제되었습니다");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID 에 해당하는 카테고리가 없습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카테고리 삭제 실패");
        }
    }
}
