package br.com.fiap.ecogenius.dto.ideia;

import br.com.fiap.ecogenius.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalhesIdeiaDto {

    private int id;
    private String nome;
    private String descricao;
    private Usuario usuario;
}
