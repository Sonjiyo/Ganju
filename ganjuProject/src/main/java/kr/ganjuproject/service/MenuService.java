package kr.ganjuproject.service;

import kr.ganjuproject.dto.MenuDTO;
import kr.ganjuproject.entity.Menu;
import kr.ganjuproject.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
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
        Collections.reverse(menus);
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
    public Menu addMenu(MultipartFile image, Menu menu) throws IOException {
        if (!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, menu.getRestaurant().getUser().getId()+"");
            menu.setMenuImage(storedFileName);
        }
        return save(menu);
    }

    @Transactional
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu getOneMenu(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateMenu(Menu menu) {
        menuRepository.save(menu);
    }

    @Transactional
    public Menu deleteImage(Long id){
        Menu menu = getOneMenu(id);
        if(menu!= null){
            s3Uploader.deleteImageFromS3(menu.getMenuImage());
            menu.setMenuImage(null);
            return save(menu);
        }
        return null;
    }

    // 메뉴 엔티티를 MenuDTO로 변환하는 메소드
    public MenuDTO convertToMenuDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setName(menu.getName());
        dto.setInfo(menu.getInfo());
        dto.setPrice(menu.getPrice());
        dto.setMenuImage(menu.getMenuImage());
        dto.setCategoryId(menu.getCategory().getId());

        // 메뉴 옵션을 DTO 형태로 변환하여 추가
        List<MenuDTO.OptionDTO> optionDTOs = menu.getOptions().stream().map(option -> {
            MenuDTO.OptionDTO optionDTO = new MenuDTO.OptionDTO();
            optionDTO.setType(option.getMenuOptionId().toString());
            optionDTO.setName(option.getContent());
            List<MenuDTO.OptionDTO.DetailDTO> detailDTOs = option.getValues().stream().map(value -> {
                MenuDTO.OptionDTO.DetailDTO detailDTO = new MenuDTO.OptionDTO.DetailDTO();
                detailDTO.setName(value.getContent());
                detailDTO.setPrice(value.getPrice());
                return detailDTO;
            }).collect(Collectors.toList());
            optionDTO.setDetails(detailDTOs);
            return optionDTO;
        }).collect(Collectors.toList());

        dto.setOptions(optionDTOs);

        return dto;
    }
}
