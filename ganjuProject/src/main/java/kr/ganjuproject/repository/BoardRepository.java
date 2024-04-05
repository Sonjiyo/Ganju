package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.RoleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByBoardCategory(RoleCategory category);
}
