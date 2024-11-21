package br.com.fiap.ecogenius.dto.comentario;

import br.com.fiap.ecogenius.model.Ideias;
import br.com.fiap.ecogenius.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalhesComentarioDto {

    private int id;
    private String comentario;
    private Usuario usuario;
    private Ideias ideia;
}
