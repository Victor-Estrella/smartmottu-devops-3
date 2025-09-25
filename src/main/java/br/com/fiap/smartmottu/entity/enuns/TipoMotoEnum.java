package br.com.fiap.smartmottu.entity.enuns;

public enum TipoMotoEnum {

    MOTTU_SPORT_110I("Mottu Sport 110i"),
    MOTTU_SPORT_ESD_2025("Mottu Sport ESD 2025");

    private final String descricao;

    TipoMotoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
