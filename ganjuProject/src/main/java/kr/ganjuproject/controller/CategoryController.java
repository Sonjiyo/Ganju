package kr.ganjuproject.controller;

import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.service.MenuOptionService;
import kr.ganjuproject.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final MenuOptionService menuOptionService;
    private final MenuService menuService;
    @GetMapping("main")
    public String menuCategory(Model model) {
        List<Menu> menus = menuService.getList();
        model.addAttribute("menus", menus);
        return "manager/menuCategory";
    }

    @GetMapping("add")
    public String addOptionForm() {
        return "manager/addMenu";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
        try {
            menuService.delete(id);
            return ResponseEntity.ok().body("메뉴가 삭제되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제 중 오류가 발생했습니다");
        }
    }

//    @PostMapping("add")
//    public String addOption(@ModelAttribute MenuOption menuOption) throws IOException {
//        menuOptionService.add(menuOption);
//        return "manager/menuCategory";
//    }
}
