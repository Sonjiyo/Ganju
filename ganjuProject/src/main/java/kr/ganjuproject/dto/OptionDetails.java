package kr.ganjuproject.dto;

import kr.ganjuproject.entity.MenuOption;
import kr.ganjuproject.entity.MenuOptionValue;
import lombok.Data;

@Data
public class OptionDetails {
    private MenuOption menuOption;
    private MenuOptionValue menuOptionValue;

    // 생성자, getter 및 setter
    public OptionDetails(MenuOption menuOption, MenuOptionValue menuOptionValue) {
        this.menuOption = menuOption;
        this.menuOptionValue = menuOptionValue;
    }
}
