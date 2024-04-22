package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 레스토랑 id 값으로 메뉴 찾기
    List<Menu> findByRestaurantId(Long restaurantId);
    List<Menu> findByMainMenu(int value);
}
