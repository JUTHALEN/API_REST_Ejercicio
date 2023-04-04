package com.example.services;

import java.util.List;

import com.example.entitites.Mascota;

public interface MascotaService {

    public List<Mascota> findAll();
    // public Mascota findById(long id);
    public Mascota save(Mascota mascota);
    public void deleteById(long id);

}
