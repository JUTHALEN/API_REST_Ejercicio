package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.dao.HotelDao;
import com.example.entitites.Hotel;

public class HotelServiceImpl implements HotelService{

    @Autowired
    private HotelDao hotelDao;

    @Override
    public List<Hotel> findAll() {
        return hotelDao.findAll();
    }

    @Override
    public Hotel findById(Long id) {
        return hotelDao.findById(id).get();
    }

    @Override
    public Hotel save(Hotel hotel) {
        return hotelDao.save(hotel);
    }

    @Override
    public void deleteById(Long id) {
        hotelDao.deleteById(id);
    }
    
}
