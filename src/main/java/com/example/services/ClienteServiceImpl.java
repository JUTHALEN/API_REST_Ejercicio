package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.dao.ClienteDao;
import com.example.entitites.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteDao clienteDao;

    @Override
    public List<Cliente> findAll(Sort sort) {
        return clienteDao.findAll(sort);
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    public Cliente findById(Long id) {
        return clienteDao.findById(id).get();
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        clienteDao.deleteById(id);
    }
    
}
