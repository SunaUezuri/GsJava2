package br.com.fiap.ecogenius.dto.empresas;

import br.com.fiap.ecogenius.model.Servico;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetalhesEmpresasDto {

    private int id;
    private String nome;
    private String cnpj;
    private String descricao;
    private Servico servico;
}
