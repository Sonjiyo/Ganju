package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.CategoryService;
import kr.ganjuproject.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final MenuService menuService;
    private final CategoryService categoryService;

    @GetMapping("/main")
    public String main(Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);
        List<Menu> menus = menuService.getList();
        model.addAttribute("menus", menus);
        return "user/main";
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
