package com.example.entitites;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String raza;
    private Genero genero;
    private LocalDate fechaNacimiento;

    public enum Genero {
        MACHO, HEMBRA
    }

    //Relacion de la tabla mascota con la tabla cliente
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Cliente cliente;
    
}
