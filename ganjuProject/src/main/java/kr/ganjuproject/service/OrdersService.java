package kr.ganjuproject.service;

import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.RoleOrders;
import kr.ganjuproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public Map<String, Object> getRestaurantOrderData(List<Orders> list){
        Map<String, Object> values = new HashMap<>();

        Long total = 0L;
        for(Orders order : list){
            total += order.getPrice();
        }

        values.put("count", list.size());
        values.put("price", total);
        return values;
    }

    public List<Orders> getRestaurantOrders(Restaurant restaurant){
        List<Orders> list = ordersRepository.findByRestaurant(restaurant);
        Collections.reverse(list);
        return list;
    }

    public List<Orders> getRestaurantOrdersWithinTimeWithoutCall(Restaurant restaurant, LocalDateTime startTime, LocalDateTime endTime) {
        List<Orders> list = ordersRepository.findByRestaurantAndDivisionNotAndRegDateBetween(restaurant, RoleOrders.CALL, startTime, endTime);
        Collections.reverse(list);
        return list;
    }

    public List<Orders> getRestaurantOrdersWithinTime(Restaurant restaurant, LocalDateTime startTime, LocalDateTime endTime) {
        List<Orders> list = ordersRepository.findByRestaurantAndRegDateBetween(restaurant, startTime, endTime);
        Collections.reverse(list);
        return list;
    }

    public List<Orders> getRestaurantOrdersDivision(Restaurant restaurant, RoleOrders role){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.minusHours(24); // 현재시간으로부터 24시간 전까지
        List<Orders> list = ordersRepository.findByRestaurantAndDivisionAndRegDateBetween(restaurant, role, startTime, currentTime);
        Collections.reverse(list);
        return list;
    }

}
