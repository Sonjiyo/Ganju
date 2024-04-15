package kr.ganjuproject.service;

import kr.ganjuproject.dto.OptionDetails;
import kr.ganjuproject.dto.OrderDTO;
import kr.ganjuproject.dto.OrderDetails;
import kr.ganjuproject.entity.*;
import kr.ganjuproject.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import org.aspectj.weaver.ast.Or;
import java.time.LocalDateTime;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final MenuService menuService;
    private final MenuOptionService menuOptionService;
    private final MenuOptionValueService menuOptionValueService;

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

    // 주문 리스트를 OrderDetails의 객체로 받아오는
    public OrderDetails getOrderDetails(OrderDTO orderDTO) {
        // 메뉴 정보 조회
       Optional<Menu> menu = menuService.findById(orderDTO.getMenuId());

        // 선택된 옵션 정보 조회
        List<OptionDetails> optionDetailsList = new ArrayList<>();
        for (OrderDTO.OptionSelection selection : orderDTO.getSelectedOptions()) {
            MenuOption menuOption = menuOptionService.findById(selection.getOptionId());
            MenuOptionValue menuOptionValue = menuOptionValueService.findById(selection.getValueId());

            OptionDetails optionDetails = new OptionDetails(menuOption, menuOptionValue);
            optionDetailsList.add(optionDetails);
        }

        return new OrderDetails(menu.get(), optionDetailsList, orderDTO.getQuantity());
    }

    //주문 거부(삭제)
    @Transactional
    public void deleteOrder(Long id){
        ordersRepository.deleteById(id);
    }

    //주문 승인
    @Transactional
    public Orders recognizeOrder(Long id){
        Orders order = ordersRepository.findById(id).orElse(null);
        if(order==null){return null;}
        order.setDivision(RoleOrders.OKAY);
        Orders orders = ordersRepository.save(order);
        return order;
    }
}
