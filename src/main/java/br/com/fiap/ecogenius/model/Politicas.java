package br.com.fiap.ecogenius.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Politicas {

    //Atributos da classe
    private int id;
    private String nome;
    private String setor;
    private double reducao; //Em porcentagem
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private Consumo consumo;
}
