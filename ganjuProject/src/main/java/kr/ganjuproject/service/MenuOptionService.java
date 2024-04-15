package kr.ganjuproject.service;

import jakarta.persistence.EntityNotFoundException;
import kr.ganjuproject.entity.MenuOption;
import kr.ganjuproject.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuOptionService {
    private final MenuOptionRepository menuOptionRepository;

    // 메뉴를 선택했을 때 가져가는 값
    public List<MenuOption> findByMenuId(Long menuId){
        return menuOptionRepository.findByMenuId(menuId);
    }

    public MenuOption findById(Long id){ return menuOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("MenuOption not found for id " + id));}
}
