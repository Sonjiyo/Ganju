package kr.ganjuproject.repository;

import kr.ganjuproject.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    // 메뉴 ID로 MenuOption 목록 찾기
    List<MenuOption> findByMenuId(Long menuId);
}
