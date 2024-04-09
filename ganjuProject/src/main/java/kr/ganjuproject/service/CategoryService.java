package kr.ganjuproject.service;

import kr.ganjuproject.dto.CategoryDTO;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 레스토랑 id 값을 가진 카테고리 목록 불러오기
    public List<CategoryDTO> findCategoriesByRestaurantId(Long restaurantId) {
        // 리포지토리에서 카테고리 엔티티 리스트 조회
        List<Category> categories = categoryRepository.findByRestaurantId(restaurantId);
        // Category 엔티티 리스트를 CategoryDTO 리스트로 변환
        return categories.stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            return dto;
        }).collect(Collectors.toList());
    }
    // DTO 말고 카테고리 가져가는것도 만들자
    public List<Category> findByRestaurantId(Long restaurantId) {
        List<Category> categories = categoryRepository.findByRestaurantId(restaurantId);

        return categories;
    }
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void add(Category category) {
        categoryRepository.save(category);
    }
}
