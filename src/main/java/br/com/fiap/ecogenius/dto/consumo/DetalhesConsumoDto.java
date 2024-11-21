package br.com.fiap.ecogenius.dto.consumo;

import br.com.fiap.ecogenius.model.Servico;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class DetalhesConsumoDto {

    private int id;
    private double previsao; //Em porcentagem
    private String condicaoClimatica;
    private int horario; //Somente as horas
    private LocalDate dataPrevisao;
    private Servico servico;
}
