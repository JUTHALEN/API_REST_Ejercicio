package com.example.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entitites.Cliente;
import com.example.entitites.Mascota;
import com.example.services.ClienteService;
import com.example.services.MascotaService;
import com.example.utilities.FileUploadUtil;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll(@RequestParam(name = "page", required = false) Integer page,
                                                @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<Cliente>> responseEntity = null;
        List<Cliente> clientes = new ArrayList<>();
        Sort sortByNombre = Sort.by("nombre");

        if (page != null && size != null) {
            try {

                Pageable pageable = PageRequest.of(page, size, sortByNombre);
                Page<Cliente> clientesPaginados = clienteService.findAll(pageable);
                clientes = clientesPaginados.getContent();
                responseEntity = new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
            
            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
             }

        } else {
            try {
                clientes = clienteService.findAll(sortByNombre);
                responseEntity = new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }    
        return responseEntity;
    }    

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable(name = "id") Integer id) {

        ResponseEntity<Map<String, Object>> responseEntity = null;

        Map<String, Object> responseAsMap = new HashMap<>();




        try {

            //Producto producto = productoService.findById(id);

            Cliente cliente = clienteService.findById(id);

            if (cliente != null) {
            String successMessage = "Se ha encontrado el cliente con id: " + id + " correctamente";
            responseAsMap.put("mensaje", successMessage);
            responseAsMap.put("cliente", cliente);
//            responseAsMap.put("mascotas", cliente.getMascotas());
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
            
        } else {

            String errorMessage = "No se ha encontrado el cliente con id: " + id;
            responseAsMap.put("error", errorMessage);
            responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.NOT_FOUND);

        }

        } catch (Exception e) {

            String errorGrave = "Error grave";
            responseAsMap.put("error", errorGrave);
            responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);

          
        }



        return responseEntity;
    }

    @PostMapping(consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<Map<String, Object>> insert(
                            @Valid 
                            @RequestPart(name = "cliente") Cliente cliente, 
                            BindingResult result,
                            @RequestPart (name = "file") MultipartFile file) throws IOException {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity;

        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            responseAsMap.put("errores", errorMessages);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        if (!file.isEmpty()) {
            String fileCode = fileUploadUtil.saveFile(file.getOriginalFilename(), file);
            cliente.setImagenCliente(fileCode + "-" + file.getOriginalFilename());
        }

        Cliente clienteDB = clienteService.save(cliente);

        try {
            if (clienteDB != null) {
                List<Mascota> mascotas = cliente.getMascotas();
                if (mascotas != null) {
                    for (Mascota mascota : mascotas) {
                        mascota.setCliente(clienteDB);
                        mascotaService.save(mascota); 
                    }
                }

                String mensaje = "El cliente y sus mascotas se han creado correctamente";
                responseAsMap.put("mensaje", mensaje);
                responseAsMap.put("cliente", clienteDB);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.CREATED);

            } else {
                String errorMensaje = "El cliente no se ha creado correctamente";
                responseAsMap.put("mensaje", errorMensaje);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (DataAccessException e) {
            String errorGrave = "Ha habido un fatal error, posible error  " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", errorGrave);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Cliente cliente, BindingResult result,
                                                      @PathVariable(name = "id") long id) {

        Map<String, Object> responseAsMap = new HashMap<>();

        ResponseEntity<Map<String, Object>> responseEntity = null;
        
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>(); 
            var prueba = result.getAllErrors();
            prueba.stream().forEach(e -> {
                errorMessages.add(e.getDefaultMessage());
            });

            responseAsMap.put("errores", errorMessages);
            responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
            return responseEntity;
         }

         // Si no hay errores, entonces persistimos el producto. 
        cliente.setId(id);
        Cliente clienteDB = clienteService.save(cliente);

        try {
            if (clienteDB != null) {

                String mensaje = "El cliente se ha actualizado correctamente" ;
                responseAsMap.put("mensaje", mensaje);
                responseAsMap.put("cliente", clienteDB);
                responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.CREATED);
    
             } else {

                String mensaje = "El cliente no se ha actualizado correctamente";    
                responseAsMap.put("mensaje", mensaje);
                responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.BAD_REQUEST );
    
             }
            
         } catch (DataAccessException e) {

            String errorGrave = "Ha habido un error, el posible error es " + e.getMostSpecificCause();
           responseAsMap.put("errorGrave", errorGrave);
           responseEntity = new ResponseEntity<Map<String,Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        
        }

        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@Valid @RequestBody Cliente cliente, 
                                         @PathVariable(name = "id") long id) {


        ResponseEntity<String> responseEntity = null;
        try {          

            Cliente clienteDelete = clienteService.findById(id);       
            if (clienteDelete != null) {
                clienteService.deleteById(id);
                String mensajeOk = "Se ha borrado correctamente.";
                responseEntity = new ResponseEntity<String>(mensajeOk, HttpStatus.OK);

            } else {
                String mensajeError = "No existe el cliente que quiere borrar.";
                responseEntity = new ResponseEntity<String>(mensajeError, HttpStatus.NOT_FOUND);
            }    
            
        } catch (DataAccessException e) {
            e.getMostSpecificCause();
            responseEntity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);            
        }
       
    return responseEntity;
        
    }


}
