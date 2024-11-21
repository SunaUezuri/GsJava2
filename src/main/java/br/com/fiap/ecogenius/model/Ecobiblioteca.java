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
public class Ecobiblioteca {

    //Atributos da classe
    private int id;
    private TipoEnergia energia;
    private String descricao;
    private Servico servico;
}
