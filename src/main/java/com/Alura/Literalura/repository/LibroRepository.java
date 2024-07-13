package com.Alura.Literalura.repository;

import com.Alura.Literalura.model.Autor;
import com.Alura.Literalura.model.Idioma;
import com.Alura.Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdiomas(Idioma idioma);

    @Query("SELECT l FROM Libro a JOIN a.autor l")
    List<Autor> mostrarAutores();



    @Query("SELECT l FROM Libro a JOIN a.autor l WHERE l.fechaDeNacimiento <= :año AND l.fechaDeMuerte >= :año")
    List<Autor> mostrarAutoresVivos(String año);
}
