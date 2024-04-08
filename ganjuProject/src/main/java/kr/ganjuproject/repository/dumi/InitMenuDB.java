package kr.ganjuproject.repository.dumi;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import kr.ganjuproject.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMenuDB {
    private final InitMenuService initMenuService;
    @PostConstruct  // 생성자 실행되면 바로 호출
    public void init() {
        initMenuService.init();
    }
    @Component
    static class InitMenuService {
        @Autowired
        EntityManager em;
        @Transactional
        public void init() {
            // 카테고리 데이터 생성
            String[] categoryNames = {"한식", "중식", "일식", "양식", "분식", "디저트"};
            for (int i = 0; i < categoryNames.length; i++) {
                Category category = new Category(null, i + 1, categoryNames[i], new ArrayList<>());
                em.persist(category);

                // 각 카테고리에 메뉴 추가
                for (int j = 0; j < 5; j++) {
                    Menu menu = new Menu();
                    menu.setName(category.getName() + " 메뉴 " + (j + 1));
                    menu.setPrice((j + 1) * 1000); // 가격 설정 예시
                    menu.setCategory(category); // 생성된 카테고리에 메뉴 설정
                    // 메뉴에 대한 추가적인 설정...

                    em.persist(menu);

                    // 카테고리 객체에 메뉴 객체 연결 (양방향 관계 설정)
                    category.getMenus().add(menu);
                }
            }
//          게시글 더미 데이터
            for (RoleCategory category : RoleCategory.values()) {
                for (int i = 1; i <= 10; i++) {
                    Board board = new Board();
                    board.setName(category.getRole() + " 작성자 " + i);
                    board.setTitle(category.getRole() + " 제목 " + i);
                    board.setContent(category.getRole() + " 내용입니다. 번호: " + i);
                    board.setRegDate(LocalDateTime.now());
                    board.setBoardCategory(category);
                    em.persist(board);
                }
            }

//          리뷰 더미 데이터
            Restaurant sampleRestaurant = em.find(Restaurant.class, 1L); // id 1인 Restaurant 인스턴스를 찾음
            Orders sampleOrder = em.find(Orders.class, 1L); // id 1인 Orders 인스턴스를 찾음

            for (int i = 1; i <= 10; i++) {
                Review review = new Review();
                review.setRestaurant(sampleRestaurant);
                review.setName("리뷰어 " + i);
                review.setContent("맛있어요! " + i);
                review.setRegDate(LocalDateTime.now());
                review.setStar((i%5) + 1);
                review.setOrder(sampleOrder);
                review.setSecret(0); // 공개 리뷰로 설정

                em.persist(review);
            }

            Users user = new Users();
            user.setLoginId("admin");
            user.setPassword("1234");
            user.setRole(RoleUsers.ROLE_ADMIN);
            em.persist(user);
            System.out.println("더미 데이터 생성 완료");
        }
    }
}