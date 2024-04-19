package kr.ganjuproject.service;

import kr.ganjuproject.dto.CategoryDTO;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 레스토랑 id 값을 가진 카테고리 목록 불러오기
    public List<CategoryDTO> findCategoriesByRestaurantId(Long restaurantId) {
        // 리포지토리에서 카테고리 엔티티 리스트 조회
        List<Category> categories = categoryRepository.findByRestaurantIdOrderByTurnAsc(restaurantId);
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
        List<Category> categories = categoryRepository.findByRestaurantIdOrderByTurnAsc(restaurantId);
        return categories;
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void add(Category category) {
        categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public void swapTurn(Long id1, Long id2) {
        Category category1 = categoryRepository.findById(id1).orElse(null);
        Category category2 = categoryRepository.findById(id2).orElse(null);
        if (category1 == null || category2 == null) {
            throw new IllegalArgumentException("주어진 ID에 해당하는 카테고리를 찾을 수 없습니다");
        }
        int tempTurn = category1.getTurn();
        category1.setTurn(category2.getTurn());
        category2.setTurn(tempTurn);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }
}
