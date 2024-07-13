package com.Alura.Literalura.principal;

import com.Alura.Literalura.model.*;
import com.Alura.Literalura.repository.LibroRepository;
import com.Alura.Literalura.service.ConsumoAPI;
import com.Alura.Literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibro = new ArrayList<>();
    private LibroRepository repositorio;

    public Principal(LibroRepository repository){ this.repositorio = repository;}
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo 
                    2 - listar libros registrados
                    3 - listar autores registrados
                    4 - listar autores vivos en un determinado año
                    5 - listar libro por idioma
                    6 - registrar libro
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            System.out.println("Ingrese la opcion que desea seleccionar");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibrosPorTitulo();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    mostrarAutoresPorAño();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Concluyendo con el challenge");
                    break;
                default:
                    System.out.println("escoja una opcion valida, por favor");

            }
        }
    }
    private DatosLibro buscarLibro(){ //buscando en la API
        System.out.println("Ingresa el nombre del que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosResultado.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado...");
            return libroBuscado.get();
        } else {
            System.out.println("libro no encontrado, intenta con otro título\n");
            return null;
        }
    }

    private void buscarLibrosPorTitulo(){
        Optional<DatosLibro> datosOp = Optional.ofNullable(buscarLibro());

        if(datosOp.isPresent()) {
            DatosLibro datos = datosOp.get();

            Libro libro = new Libro(datos);
            List<Autor> autores = new ArrayList<>();
            for (DatosAutor datosAutor : datos.autor()) {
                Autor autor = new Autor(datosAutor);
                autor.setLibro(libro);
                autores.add(autor);
            }
            libro.setAutor(autores);
            try {
                repositorio.save(libro);
                System.out.println(libro.getTitulo() + "El libro se registro existosamente :D");
            } catch (DataIntegrityViolationException e) {
                System.out.println("Error, no se pudo registrar el libro.");
            }
        }}
        private void listarLibros(){
            List<Libro> mostrarListaLibros = repositorio.findAll();
            mostrarListaLibros.forEach(l -> System.out.println(
                    "+++++++++ LIBRO +++++++++" +
                            "\nTítulo: " + l.getTitulo()+
                            "\nIdioma: " + l.getIdiomas()+
                            "\nAutor: " + l.getAutor().stream().map(Autor::getNombre).collect(Collectors.joining()) +
                            "\nNúmero de descargas: " + l.getNumeroDescargas() +
                            "\n"
            ));
    }
    private void listarAutores(){
        List<Autor> mostarListaAutores = repositorio.mostrarAutores();

        Map<String, List<String>> autoresConLibros = mostarListaAutores.stream()
                .collect(Collectors.groupingBy(
                        Autor::getNombre,
                        Collectors.mapping(a -> a.getLibro().getTitulo(), Collectors.toList())
                ));

        autoresConLibros.forEach((nombre, libros) -> {
            Autor autor = mostarListaAutores.stream()
                    .filter(a -> a.getNombre().equals(nombre))
                    .findFirst().orElse(null);
            if (autor != null) {
                System.out.println("+++++++++ AUTOR +++++++++");
                System.out.println("Nombre: " + nombre);
                System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de muerte: " + autor.getFechaDeMuerte());
                System.out.println("Libros: " + libros + "\n");
            }
        });

    }

    public void mostrarAutoresPorAño(){
        System.out.println("Ingresa el año a consultar:");
        String año = teclado.nextLine();

        List<Autor> autoresVivos = repositorio.mostrarAutoresVivos(año);

        if (autoresVivos.isEmpty()){
            System.out.println("Sin autores vivos en el año indicado...\n");
            return;
        }

        Map<String, List<String>> autoresConLibros = autoresVivos.stream()
                .collect(Collectors.groupingBy(
                        Autor::getNombre,
                        Collectors.mapping(a -> a.getLibro().getTitulo(), Collectors.toList())
                ));

        autoresConLibros.forEach((nombre, libros) -> {
            Autor autor = autoresVivos.stream()
                    .filter(a -> a.getNombre().equals(nombre))
                    .findFirst().orElse(null);
            if (autor != null) {
                System.out.println("+++++++++ AUTOR +++++++++");
                System.out.println("Nombre: " + nombre);
                System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de muerte: " + autor.getFechaDeMuerte());
                System.out.println("Libros: " + libros + "\n");
            }
        });
    }
    public void mostrarLibrosPorIdioma(){
            System.out.println("""
                Escriba el idioma del libro:
                ES: Español
                EN: Ingles
                FR: Frances
                IT: Italiano
                PT: Portugues
                """);

            var idiomaSelecionado = teclado.nextLine();

            try {
                List<Libro> libroPorIdioma = repositorio.findByIdiomas(Idioma.valueOf(idiomaSelecionado.toUpperCase()));
                libroPorIdioma.forEach(n -> System.out.println(
                        "+++++++++ LIBRO +++++++++" +
                                "\nTitulo: " + n.getTitulo() +
                                "\nIndioma: " + n.getIdiomas() +
                                "\nAutor: " + n.getAutor().stream().map(Autor::getNombre).collect(Collectors.joining()) +
                                "\nNumero de descargas: " + n.getNumeroDescargas() +
                                "\n"
                ));
            } catch (IllegalArgumentException e){
                System.out.println("Idioma no existe...\n");
            }

        }











}













