package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entitites.Mascota;

public interface MascotaDao extends JpaRepository<Mascota, Long>{
    
}
