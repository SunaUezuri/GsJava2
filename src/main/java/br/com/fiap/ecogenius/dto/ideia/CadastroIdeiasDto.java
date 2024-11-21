package br.com.fiap.ecogenius.dto.ideia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CadastroIdeiasDto {

    @NotBlank @Size(max = 255)
    private String nome;
    @NotBlank @Size(max = 4000)
    private String descricao;
    @NotNull
    private int idUser;
}
