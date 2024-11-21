package br.com.fiap.ecogenius.model;

import lombok.*;

import java.time.LocalDate;

/*
* Criando os Getters, Setters, Construtores
* e ToString da classe
* */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {

    //Atributos da classe Usuário
    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    public Usuario(int id){
        this.id = id;
    }
}
