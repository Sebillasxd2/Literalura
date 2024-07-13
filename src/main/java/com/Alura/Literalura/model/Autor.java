package com.Alura.Literalura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaDeNacimiento;
    private String fechaDeMuerte;
    @ManyToOne
    private Libro libro;

    public Autor() {}

    public Autor(DatosAutor d) {
        this.nombre = d.nombre();
        this.fechaDeMuerte = d.fechaDeMuerte();
        this.fechaDeNacimiento = d.fechaDeNacimiento();

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeMuerte() {
        return fechaDeMuerte;
    }

    public void setFechaDeMuerte(String fechaDeMuerte) {
        this.fechaDeMuerte = fechaDeMuerte;
    }
    public Libro getLibro() {

        return libro;
    }

    public void setLibro(Libro libro) {

        this.libro = libro;
    }


    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nombre='" + nombre + "\n" +
                ", fechaDeNacimiento=" + fechaDeNacimiento +"\n"+
                ", fechaDeMuerte=" + fechaDeMuerte +"\n"+
                "Libro: " + libro +
                '}';
    }
}

