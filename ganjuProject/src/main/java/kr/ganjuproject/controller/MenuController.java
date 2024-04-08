package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.CategoryService;
import kr.ganjuproject.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final CategoryService categoryService;

    // 메인 메뉴 첫 페이지
    @GetMapping("/main")
    public String main(Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);
        List<Menu> menus = menuService.getList();
        model.addAttribute("menus", menus);
        return "user/main";
    }

    // 비동기 메인 메뉴 데이터
    @GetMapping("/validateMenuMenu")
    @ResponseBody
    public ResponseEntity<Map<String, Object>>  validateMenu(Model model) {
        System.out.println("비동기 메뉴");
        Map<String, Object> response = new HashMap<>();
        List<Category> categories = categoryService.getList();
        List<Menu> menus = menuService.getList();

        response.put("categories", categories);
        response.put("menus", menus);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public String info(@RequestParam Long id, Model model){
        Optional<Menu> menu = menuService.findById(id);

        if(menu.isPresent()) {
            Menu m = menu.get();
            model.addAttribute("menu", m);
            return"user/info";
        }else{
            return "redirect:/user/main";
        }
    }

    @PostMapping("/info")
    public String info(){

        return "user/cart";
    }

    @PostMapping("/cart")
    public String cart(){

        return"user/order";
    }
    @PostMapping("/order")
    public String order(){
        return"user/order";
    }

    @GetMapping("/review")
    public String review(){
        return"user/review";
    }

    @PostMapping("/review")
    public String review(Model model){
        return"redirect:/user/main";
    }
}
