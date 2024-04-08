package kr.ganjuproject.service;

import kr.ganjuproject.entity.Restaurant;
import kr.ganjuproject.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final S3Uploader s3Uploader;

    public List<Restaurant> getRestaurantList(){return restaurantRepository.findAll();}
    @Transactional
    public Restaurant insertRestaurant(MultipartFile image, Restaurant restaurant) throws IOException {
        System.out.println("image = " + image);
        if(!image.isEmpty()){
            String storedFileName = s3Uploader.upload(image, "images");
            restaurant.setLogo(storedFileName);
        }
        return restaurantRepository.save(restaurant);
    }


}
