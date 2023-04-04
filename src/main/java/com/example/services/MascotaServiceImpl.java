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

    // @Override
    // public Mascota findById(long id) {
    //     return mascotaDao.findById(id);
    // }

    @Override
    public Mascota save(Mascota mascota) {
        return mascotaDao.save(mascota);
    }

    @Override
    public void deleteById(long id) {
        mascotaDao.deleteById(id);
    }
    
}
