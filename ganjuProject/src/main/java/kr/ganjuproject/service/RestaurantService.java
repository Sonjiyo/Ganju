package kr.ganjuproject.service;

import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.repository.ManagerRepository;
import kr.ganjuproject.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;

    private final S3Uploader s3Uploader;

    public List<Restaurant> getRestaurantList() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        Collections.reverse(restaurantList);
        return restaurantList;
    }

    @Transactional
    public Restaurant insertRestaurant(MultipartFile image, Restaurant restaurant) throws IOException {
        if (!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, restaurant.getUser().getId()+"");
            restaurant.setLogo(storedFileName);
        }
        return save(restaurant);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void delete(Restaurant restaurant) {
        restaurantRepository.delete(restaurant);
    }

    @Transactional
    public Restaurant updateRestaurant(MultipartFile image, Restaurant restaurant) throws IOException {
        if (!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, restaurant.getId()+"");
            restaurant.setLogo(storedFileName);
        }
        return save(restaurant);
    }

    public void deleteLogo(String imageUrl){
        s3Uploader.deleteImageFromS3(imageUrl);
    }
}
