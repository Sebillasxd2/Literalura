package com.Alura.Literalura.model;

public enum Idioma {
    ES("es"),
    EN("en"),
    FR("fr"),
    IT("it"),
    PT("pt");

    private String i;

    Idioma (String idioma){
        this.i = idioma;
    }

    public static Idioma fromString (String text){
        for (Idioma idioma : Idioma.values()){
            if (idioma.i.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("Error buscando el idioma de este libro" +  text);
    }
}
