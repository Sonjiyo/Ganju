package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//  평균 별점 구하기
    @Query("SELECT AVG(r.star) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(Long restaurantId);

//  해당 식당의 리뷰만
    Page<Review> findByRestaurantId(Long restaurantId, Pageable pageable);
}
