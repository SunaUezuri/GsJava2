package br.com.fiap.ecogenius.dto.ecobiblioteca;

import br.com.fiap.ecogenius.model.Servico;
import br.com.fiap.ecogenius.model.TipoEnergia;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalhesEcobibliotecaDto {

    private int id;
    private TipoEnergia energia;
    private String descricao;
    private Servico servico;
}
