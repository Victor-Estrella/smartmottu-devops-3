package br.com.fiap.smartmottu.entity.enuns;

public enum StatusEnum {

    ATIVO("Ativo", 1L),
    INATIVO("Inativo", 2L),
    BLOQUEADO("Bloqueado", 3L),
    CANCELADO("Cancelado", 4L),
    MANUTENCAO("Manutenção", 5L);

    private final String descricao;
    private final Long id;

    public Long getId() {
        return id;
    }

    StatusEnum(String descricao, Long id) {
        this.descricao = descricao;
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
}
