package kr.ganjuproject.repository;

import kr.ganjuproject.entity.MenuOption;
import kr.ganjuproject.entity.MenuOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionValueRepository extends JpaRepository<MenuOptionValue, Long> {
    List<MenuOptionValue> findByMenuOptionId(Long menuOptionId);
}
