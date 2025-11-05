package br.com.fiap.smartmottu.entity.enuns;

public enum StatusAluguel {

    ATIVO("Ativo"),
    INATIVO("Inativo"),
    PENDENTE("Pendente");

    private final String descricao;

    StatusAluguel(String descricao) {
        this.descricao = descricao;
    }
}
