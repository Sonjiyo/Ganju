package kr.ganjuproject.service;

import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public List<Menu> getList() {
        return menuRepository.findAll();
    }

    public Optional<Menu> findById(Long id){
        return menuRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    @Transactional
    public void add(Menu menu) {
        menuRepository.save(menu);
    }
}
