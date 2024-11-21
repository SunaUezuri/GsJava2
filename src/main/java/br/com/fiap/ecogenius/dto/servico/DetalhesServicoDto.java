package br.com.fiap.ecogenius.dto.servico;

import br.com.fiap.ecogenius.model.TipoServico;
import br.com.fiap.ecogenius.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalhesServicoDto {

    private int id;
    private String nome;
    private String descricao;
    private TipoServico tipo;
    private Usuario usuario;

}
