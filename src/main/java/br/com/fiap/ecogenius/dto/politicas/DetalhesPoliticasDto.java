package br.com.fiap.ecogenius.dto.politicas;

import br.com.fiap.ecogenius.model.Consumo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class DetalhesPoliticasDto {

    private int id;
    private String nome;
    private String setor;
    private double reducao; //Em porcentagem
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private Consumo consumo;
}
