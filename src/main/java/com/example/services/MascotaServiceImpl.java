package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.dao.MascotaDao;
import com.example.entitites.Mascota;

public class MascotaServiceImpl implements MascotaService {

    @Autowired
    private MascotaDao mascotaDao;

    @Override
    public List<Mascota> findAll() {
        return mascotaDao.findAll();
    }

    @Override
    public Mascota findById(Long id) {
        return mascotaDao.findById(id).get();
    }

    @Override
    public Mascota save(Mascota mascota) {
        return mascotaDao.save(mascota);
    }

    @Override
    public void deleteById(Long id) {
        mascotaDao.deleteById(id);
    }
    
}
