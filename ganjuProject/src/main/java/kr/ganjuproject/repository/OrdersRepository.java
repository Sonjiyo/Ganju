package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.RoleOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByRestaurant(Restaurant restaurant);
    List<Orders> findByRestaurantAndRegDateBetween(Restaurant restaurant, LocalDateTime startDate, LocalDateTime endDate);
    List<Orders> findByRestaurantAndDivisionNotAndRegDateBetween(Restaurant restaurant, RoleOrders divisionToExclude, LocalDateTime startDate, LocalDateTime endDate);
    List<Orders> findByRestaurantAndDivisionAndRegDateBetween(Restaurant restaurant, RoleOrders division, LocalDateTime startDate, LocalDateTime endDate);
}
