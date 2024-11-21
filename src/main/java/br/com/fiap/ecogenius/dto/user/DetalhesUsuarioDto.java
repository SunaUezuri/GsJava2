package br.com.fiap.ecogenius.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class DetalhesUsuarioDto {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
}
