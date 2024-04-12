package kr.ganjuproject.service;

import kr.ganjuproject.entity.Orders;
import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public Map<String, Object> getRestaurantOrderData(Restaurant restaurant){
        List<Orders> list = ordersRepository.findByRestaurant(restaurant);
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
        return ordersRepository.findByRestaurant(restaurant);
    }

}
