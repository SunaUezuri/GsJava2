package br.com.fiap.ecogenius.dto.ecobiblioteca;

import br.com.fiap.ecogenius.model.Servico;
import br.com.fiap.ecogenius.model.TipoEnergia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AtualizaEcobibliotecaDto {

    @NotBlank
    @Size(max = 255)
    private TipoEnergia energia;
    @NotBlank @Size(max = 1000)
    private String descricao;
    @NotNull
    private int idServico;
}
