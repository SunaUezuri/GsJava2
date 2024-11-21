package br.com.fiap.ecogenius.dto.consumo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class CadastroConsumoDto {

    @NotNull
    private double previsao; //Em porcentagem
    @NotBlank @Size(max = 50)
    private String condicaoClimatica;
    @NotNull @Max(2)
    private int horario; //Somente as horas
    @NotNull
    private LocalDate dataPrevisao;
    @NotNull
    private int idServico;
}
