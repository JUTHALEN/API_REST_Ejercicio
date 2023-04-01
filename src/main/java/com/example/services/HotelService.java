package com.example.services;

import java.util.List;

import com.example.entitites.Hotel;

public interface HotelService {
    public List<Hotel> findAll();
    public Hotel findById(Long id);
    public Hotel save(Hotel hotel);
    public void deleteById(Long id);
    
}
