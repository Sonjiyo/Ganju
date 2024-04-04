package kr.ganjuproject.controller;

import kr.ganjuproject.service.MenuOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final MenuOptionService menuOptionService;
    @GetMapping("main")
    public String menuCategory() {
        return "manager/menuCategory";
    }

    @GetMapping("add")
    public String addOptionForm() {
        return "manager/addMenu";
    }

//    @PostMapping("add")
//    public String addOption(@ModelAttribute MenuOption menuOption) throws IOException {
//        menuOptionService.add(menuOption);
//        return "manager/menuCategory";
//    }
}
