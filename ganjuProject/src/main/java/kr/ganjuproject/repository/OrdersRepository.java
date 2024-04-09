package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByRestaurant(Restaurant restaurant);
}
