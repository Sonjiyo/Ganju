package kr.ganjuproject.service;

import kr.ganjuproject.entity.MenuOption;
import kr.ganjuproject.entity.MenuOptionValue;
import kr.ganjuproject.repository.MenuOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuOptionValueService {
    private final MenuOptionValueRepository menuOptionValueRepository;

    public List<MenuOptionValue> findByMenuOptionId(Long menuOptionId){
        return menuOptionValueRepository.findByMenuOptionId(menuOptionId);
    }

}
