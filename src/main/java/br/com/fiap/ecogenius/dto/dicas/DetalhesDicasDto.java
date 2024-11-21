package br.com.fiap.ecogenius.dto.dicas;

import br.com.fiap.ecogenius.model.Servico;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalhesDicasDto {

    private int id;
    private String nome;
    private String descricao;
    private String link;
    private Servico servico;
}
