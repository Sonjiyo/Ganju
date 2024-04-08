package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.RoleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {


    // 메인 메뉴에서 비동기로 공지사항만 가져갈때 쓰는 메서드
    @Query("SELECT b FROM Board b WHERE b.boardCategory = 'NOTICE'")
    List<Board> noticeGetList();

    List<Board> findByBoardCategory(RoleCategory category);

}
