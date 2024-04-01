package kr.ganjuproject.service;

import kr.ganjuproject.repository.RestaurantImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantImageService {
    private final RestaurantImageRepository restaurantImageRepository;
}
