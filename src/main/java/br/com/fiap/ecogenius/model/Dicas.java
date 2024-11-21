package br.com.fiap.ecogenius.model;

import lombok.*;

/*
 * Criando os Getters, Setters, Construtores
 * e ToString da classe
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dicas {

    //Atributos da classe
    private int id;
    private String nome;
    private String descricao;
    private String link;
    private Servico servico;
}
