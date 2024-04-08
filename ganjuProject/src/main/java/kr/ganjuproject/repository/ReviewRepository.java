package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    평균 별점 구하기
    @Query("SELECT AVG(r.star) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(Long restaurantId);

}
