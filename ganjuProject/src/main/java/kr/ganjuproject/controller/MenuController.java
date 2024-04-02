package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.CategoryService;
import kr.ganjuproject.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final CategoryService categoryService;

    @GetMapping("/main")
    public String main(Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);
        List<Menu> menus = menuService.getList();
        model.addAttribute("menus", menus);
        return "menu/main";
    }

    @GetMapping("/info/{id}")
    public String info(@PathVariable Long id, Model model){
        Optional<Menu> menu = menuService.findById(id);

        if(menu.isPresent()) {
            Menu m = menu.get();
            model.addAttribute("menu", m);
            return"menu/info";
        }else{
            return "redirect:/menu/main";
        }
    }
    @GetMapping("/cart")
    public String cart(){
        return"menu/cart";
    }
    @GetMapping("/order")
    public String order(){
        return"menu/order";
    }
    @GetMapping("/review")
    public String review(){
        return"menu/review";
    }
}
