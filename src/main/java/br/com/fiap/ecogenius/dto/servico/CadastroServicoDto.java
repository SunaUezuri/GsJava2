package br.com.fiap.ecogenius.dto.servico;

import br.com.fiap.ecogenius.model.TipoServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CadastroServicoDto {

    @NotBlank @Size(max = 255)
    private String nome;
    @NotBlank @Size(max = 4000)
    private String descricao;
    @NotBlank @Size(max = 100)
    private TipoServico tipo;
    @NotNull
    private int idUsuario;

}
