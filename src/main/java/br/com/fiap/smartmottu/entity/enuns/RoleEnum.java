package br.com.fiap.smartmottu.entity.enuns;

public enum RoleEnum {
    ADMIN("Admin"),
    USER("User");

    private final String descricao;

    RoleEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}