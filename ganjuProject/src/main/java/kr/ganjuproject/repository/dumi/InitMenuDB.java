package kr.ganjuproject.repository.dumi;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import kr.ganjuproject.entity.Category;
import kr.ganjuproject.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
            System.out.println("더미 데이터 생성 완료");
        }
    }
}
