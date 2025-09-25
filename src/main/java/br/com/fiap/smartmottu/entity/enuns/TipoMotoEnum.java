package br.com.fiap.smartmottu.entity.enuns;

public enum TipoMotoEnum {

    MOTTU_SPORT_110I("Mottu Sport 110i"),
    MOTTU_SPORT_ESD_2025("Mottu Sport ESD 2025"),
    MOTTU_POP_100("Mottu Pop 100"),
    MOTTU_POP_150("Mottu Pop 150"),
    MOTTU_ELETRICA_X("Mottu Elétrica X");
    
    private final String descricao;

    TipoMotoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
