package br.com.fiap.ecogenius.dto.dicas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AtualizaDicasDto {

    @NotBlank
    @Size(max = 255)
    private String nome;
    @NotBlank @Size(max = 4000)
    private String descricao;
    @NotBlank @Size(max = 100)
    private String link;
    @NotNull
    private int idServico;
}
