package br.com.fiap.ecogenius.dto.politicas;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class AtualizaPoliticasDto {

    @NotBlank
    @Size(max = 50)
    private String nome;
    @NotBlank @Size(max = 100)
    private String setor;
    @NotNull
    @Max(100)
    private double reducao; //Em porcentagem
    @NotNull
    private LocalDate dataInicio;
    @Future
    private LocalDate dataTermino;
    @NotNull
    private int idConsumo;
}
