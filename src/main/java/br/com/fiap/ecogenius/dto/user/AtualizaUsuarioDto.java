package br.com.fiap.ecogenius.dto.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class AtualizaUsuarioDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank @Email
    @Size(max = 150)
    private String email;

    @NotBlank @Size(max = 255)
    private String senha;

    @Past
    @NotNull
    private LocalDate dataNascimento;
}
