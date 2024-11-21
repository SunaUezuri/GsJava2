package br.com.fiap.ecogenius.dto.comentario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CadastroComentarioDto {

    @NotBlank @Size(max = 4000)
    private String comentario;
    @NotNull
    private int ideiaId;
    @NotNull
    private int idUser;
}
