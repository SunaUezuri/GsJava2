package br.com.fiap.ecogenius.model;

import lombok.*;

import java.time.LocalDate;

/*
 * Criando os Getters, Setters, Construtores
 * e ToString da classe
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Consumo {

    //Atributos da classe
    private int id;
    private double previsao; //Em porcentagem
    private String condicaoClimatica;
    private int horario; //Somente as horas
    private LocalDate dataPrevisao;

    private Servico servico;

    public Consumo(int id){
        this.id = id;
    }
}
