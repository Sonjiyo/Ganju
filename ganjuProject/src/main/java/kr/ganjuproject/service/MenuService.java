package kr.ganjuproject.service;

import kr.ganjuproject.dto.MenuDTO;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final S3Uploader s3Uploader;

    // 레스토랑 id 값으로 메뉴 전체 불러오기
    public List<MenuDTO> findMenusByRestaurantId(Long restaurantId) {
        // 리포지토리에서 메뉴 엔티티 리스트 조회
        List<Menu> menus = menuRepository.findByRestaurantId(restaurantId);
        // Menu 엔티티 리스트를 MenuDTO 리스트로 변환
        return menus.stream().map(menu -> {
            MenuDTO dto = new MenuDTO();
            dto.setId(menu.getId());
            dto.setName(menu.getName());
            dto.setInfo(menu.getInfo());
            dto.setPrice(menu.getPrice());
            dto.setMenuImage(menu.getMenuImage());
            dto.setCategoryId(menu.getCategory().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    // 여기도 마찬가지로 DTO 말고 Menu 값 보내는거
    public List<Menu> findByRestaurantId(Long restaurantId) {
        // 리포지토리에서 메뉴 엔티티 리스트 조회
        List<Menu> menus = menuRepository.findByRestaurantId(restaurantId);
        // Menu 엔티티 리스트를 MenuDTO 리스트로 변환
        return menus;
    }

    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    @Transactional
    public void add(Menu menu, MultipartFile image) throws IOException {
        if(!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image);
            menu.setMenuImage(storedFileName);
        }
        menuRepository.save(menu);
    }

    public Menu getOneMenu(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateMenu(Menu menu) {
        menuRepository.save(menu);
    }
}
