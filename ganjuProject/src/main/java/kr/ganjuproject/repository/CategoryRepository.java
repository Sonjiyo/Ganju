package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 레스토랑 id 값으로 카테고리 찾기
    List<Category> findByRestaurantId(Long restaurantId);
}
