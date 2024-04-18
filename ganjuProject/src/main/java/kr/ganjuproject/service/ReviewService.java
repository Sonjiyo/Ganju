package kr.ganjuproject.service;

import kr.ganjuproject.dto.ReviewDTO;
import kr.ganjuproject.entity.Review;
import kr.ganjuproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableJpaAuditing // Spring Data JPA가 @CreatedDate 어노테이션이 붙은 필드를 자동으로 관리하게 하려면
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 새 리뷰 저장
    @Transactional
    public void save(Review review){
        reviewRepository.save(review);
    }
    // 메인 메뉴에서 비동기로 리뷰만 가져갈때 쓰는 메서드
    public List<ReviewDTO> reviewGetList(Long restaurantId, int page) {
        // PageRequest.of의 첫 번째 파라미터는 페이지 번호(0부터 시작), 두 번째 파라미터는 페이지 당 항목 수입니다.
        // 생성 시간을 기준으로 내림차순 정렬합니다.
        int num = 5;
        PageRequest pageRequest = PageRequest.of(page, num, Sort.by("regDate").descending());
        Page<Review> reviews = reviewRepository.findByRestaurantId(restaurantId, pageRequest);

        return reviews.getContent().stream().map(this::convertToReviewDTO).collect(Collectors.toList());
    }

    // 평점 구하는 어쩌구 저쩌구
    public Double getAverageRating(Long restaurantId) {
        // restaurantId에 해당하는 식당의 평균 별점을 조회
        Double averageRating = reviewRepository.findAverageRatingByRestaurantId(restaurantId);
        return averageRating != null ? Math.round(averageRating * 10) / 10.0 : 0.0; // 소수점 한 자리까지 반올림
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

    // Review 엔티티를 ReviewDTO로 변환하는 메서드
    private ReviewDTO convertToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setName(review.getName()); // Review 엔티티에서 이름을 가져오는 방법 확인 필요
        reviewDTO.setContent(review.getContent());
        reviewDTO.setStar(review.getStar());
        reviewDTO.setRegDate(review.getRegDate());
        reviewDTO.setSecret(review.getSecret());
        return reviewDTO;
    }

    public long countReviews() {
        return reviewRepository.count();
    }
}
