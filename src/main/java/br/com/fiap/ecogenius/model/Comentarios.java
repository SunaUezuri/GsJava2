package br.com.fiap.ecogenius.model;

import lombok.*;

/*
 * Criando os Getters, Setters, Construtores
 * e ToString da classe
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comentarios {

    //Atributos da classe
    private int id;
    private String comentario;
    private Ideias ideia;
    private Usuario usuario;
}
