package br.com.fiap.ecogenius.dto.erro;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CampoErroDto {

    private String campo;
    private String mensagem;
}
