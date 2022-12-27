package com.cg.microservices.ratingservice.repositories;

import com.cg.microservices.ratingservice.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating,String> {

    //custom methods
    List<Rating> findByUserId(String userId);
    List<Rating> findByHotelId(String hotelId);
}
