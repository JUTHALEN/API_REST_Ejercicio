package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entitites.Hotel;

public interface HotelDao extends JpaRepository<Hotel, Long>{
    
}
