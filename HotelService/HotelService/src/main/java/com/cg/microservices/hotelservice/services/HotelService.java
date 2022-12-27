package com.cg.microservices.hotelservice.services;

import com.cg.microservices.hotelservice.entities.Hotel;

import java.util.List;

public interface HotelService {

    //create
    Hotel createHotel(Hotel hotel);

    //get single
    Hotel getHotel(String hotelId);

    //get all
    List<Hotel> getAllHotel();
}
