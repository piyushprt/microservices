package com.cg.microservices.userservice.services;

import com.cg.microservices.userservice.entities.Hotel;
import com.cg.microservices.userservice.entities.Rating;
import com.cg.microservices.userservice.entities.User;
import com.cg.microservices.userservice.exceptions.ResourceNotFoundException;
import com.cg.microservices.userservice.external.services.HotelService;
import com.cg.microservices.userservice.external.services.RatingService;
import com.cg.microservices.userservice.repositories.UserRepository;
import feign.FeignException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RatingService ratingService;

    @Override
    public User createUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
//            Rating[] ratingForUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
//
//            List<Rating> ratingList = Arrays.stream(ratingForUser).toList();
            List<Rating> ratingList = ratingService.getRatings();
//            List<Rating> ratings = new ArrayList<>();
//            ratingList.forEach(rating -> {
//                //Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(),Hotel.class);
//                Hotel hotel = hotelService.getHotel(rating.getHotelId());
//                rating.setHotel(hotel);
//                ratings.add(rating);
//            });

            List<Rating> ratings = ratingList.stream().map(rating-> {
                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());

            user.setRatings(ratings);
        });

        return users;
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server: " + userId));
        //fetch rating for above user from rating repository
        //http://localhost:8083/ratings/users/d2756aae-68d7-49c9-86bc-fa59c3eaf8a2
        Rating[] ratingForUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);

        List<Rating> ratingList = Arrays.stream(ratingForUser).toList();

//        List<Rating> ratings = ratingList.stream().map(rating -> {
//            http://localhost:8083/ratings/hotels/cedc5e2a-1dfc-4488-be17-602a82c731f5
//            Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(),Hotel.class);
//            rating.setHotel(hotel);
//            return rating;
//        }).collect(Collectors.toList());

        List<Rating> ratings = new ArrayList<>();

        ratingList.forEach(rating -> {
            Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            rating.setHotel(hotel);
            ratings.add(rating);
        });

        user.setRatings(ratings);

        return user;
    }
}
