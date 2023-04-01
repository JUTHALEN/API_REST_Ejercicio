package com.example.entitites;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 25, message="El nombre tiene que tener entre 3 y 25 caracteres.")
    private String nombre;
    private String apellidos;
    private LocalDate fechaAlta;

    //Relacion con la tabla mascota, un cliente puede tener varias mascotas
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "cliente")
    private List<Mascota> mascotas;

    //Relacion con hotel, un cliente solo puede tener mascotas en un hotel
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Hotel hotel;

}
