package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Board;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.RoleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {


    // 메인 메뉴에서 비동기로 공지사항만 가져갈때 쓰는 메서드 
    // 더보기 처리 때문에 안씀
//    @Query("SELECT b FROM Board b WHERE b.boardCategory = 'NOTICE'")
//    List<Board> noticeGetList();

    // 특정 식당의 특정 카테고리(예: 공지사항)에 속하는 게시글을 페이징 처리하여 가져오기
    Page<Board> findByRestaurantIdAndBoardCategory(Long restaurantId, RoleCategory boardCategory, Pageable pageable);

    List<Board> findByRestaurantIdAndBoardCategory(Long restaurantId, RoleCategory boardCategory);


    //  해당 식당의 공지사항의 사이즈 값
    @Query("SELECT COUNT(b) FROM Board b WHERE b.restaurant.id = :restaurantId AND b.boardCategory = :boardCategory")
    Long countByRestaurantIdAndBoardCategory(@Param("restaurantId") Long restaurantId, @Param("boardCategory") RoleCategory boardCategory);

    List<Board> findByBoardCategory(RoleCategory category);

    List<Board> findByBoardCategoryAndTitleAndRestaurant(RoleCategory category, String title, Restaurant restaurant);

    List<Board> findByBoardCategoryAndTitleNot(RoleCategory category, String title);
}
