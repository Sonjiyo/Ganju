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

    @GetMapping("main")
    public String menuCategory(Model model) {
        List<Menu> menus = menuService.getList();
        model.addAttribute("menus", menus);
        return "manager/menuCategory";
    }

    @GetMapping("add")
    public String addMenuForm(Model model) {
        List<Menu> menus = menuService.getList();
        model.addAttribute("menus", menus);
        return "manager/addMenu";
    }

    @PostMapping("add")
    public ResponseEntity<String> addMenu(@RequestBody Menu menu) {
        try {
            menuService.add(menu);
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

//    @PostMapping("add")
//    public String addOption(@ModelAttribute MenuOption menuOption) throws IOException {
//        menuOptionService.add(menuOption);
//        return "manager/menuCategory";
//    }
}
