package kr.ganjuproject.service;

import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getRestaurantList(){return restaurantRepository.findAll();}
    @Transactional
    public Restaurant insertRestaurant(Restaurant restaurant){return restaurantRepository.save(restaurant);}
}
