package com.Alura.Literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idiomas;
    private Double numeroDescargas;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autor;

    public Libro(){}

    public Libro(DatosLibro d) {
        this.titulo = d.titulo();
        this.idiomas =Idioma.fromString(d.idiomas().stream().limit(1).collect(Collectors.joining()));
        this.numeroDescargas = d.numeroDescargas();
    }
    @Override
    public String toString() {
        return "********************************"+'\'' +
                "titulo=" + titulo +
                "autor='" + autor + '\'' +
                ", totalTemporadas=" + numeroDescargas +
                ", idioma=" + idiomas  +
                ", numero De Descargas='" + numeroDescargas +'\''+
                "*******************************************";

    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Idioma idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        autor.forEach(a-> a.setLibro(this));
        this.autor = autor;
    }
}
