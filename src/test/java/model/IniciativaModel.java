package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IniciativaModel {

    @Expose
    private Long reducaoCarbonoId;

    @Expose
    private String descricao;

    @Expose
    private String categoria;

    @Expose
    private String status;

    @Expose
    private BigDecimal emissaoBaseTco2E;

    @Expose
    private BigDecimal metaReducaoTco2E;
}
