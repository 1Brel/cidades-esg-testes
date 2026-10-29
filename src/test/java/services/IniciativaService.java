package services;

import io.restassured.response.Response;
import model.IniciativaModel;

import java.math.BigDecimal;

public class IniciativaService extends BaseService {

    private static final String ENDPOINT = "/api/reducao-carbono";

    public IniciativaModel iniciativaValida() {
        IniciativaModel iniciativa = new IniciativaModel();
        iniciativa.setDescricao("Instalação de painéis solares em escolas municipais");
        iniciativa.setCategoria("Energia Renovável");
        iniciativa.setStatus("PLANEJADA");
        iniciativa.setEmissaoBaseTco2E(new BigDecimal("3200.00"));
        iniciativa.setMetaReducaoTco2E(new BigDecimal("1800.00"));
        return iniciativa;
    }

    public void definirCampo(IniciativaModel iniciativa, String campo, String valor) {
        switch (campo) {
            case "descricao" -> iniciativa.setDescricao(valor);
            case "categoria" -> iniciativa.setCategoria(valor);
            case "status" -> iniciativa.setStatus(valor);
            case "emissaoBaseTco2E" -> iniciativa.setEmissaoBaseTco2E(paraNumero(valor));
            case "metaReducaoTco2E" -> iniciativa.setMetaReducaoTco2E(paraNumero(valor));
            default -> throw new IllegalArgumentException("Campo inesperado: " + campo);
        }
    }

    private BigDecimal paraNumero(String valor) {
        return (valor == null || valor.isBlank()) ? null : new BigDecimal(valor.trim());
    }

    public IniciativaModel copiar(IniciativaModel original) {
        return gson.fromJson(gson.toJson(original), IniciativaModel.class);
    }

    public Response cadastrar(IniciativaModel iniciativa, String token) {
        return requisicao(token)
                .body(gson.toJson(iniciativa))
                .when()
                .post(BASE_URL + ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public Response listar(String token) {
        return requisicao(token)
                .when()
                .get(BASE_URL + ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public Response buscarPorId(Long id, String token) {
        return requisicao(token)
                .when()
                .get(BASE_URL + ENDPOINT + "/" + id)
                .then()
                .extract()
                .response();
    }

    public Response atualizar(IniciativaModel iniciativa, String token) {
        return requisicao(token)
                .body(gson.toJson(iniciativa))
                .when()
                .put(BASE_URL + ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public Response excluir(Long id, String token) {
        return requisicao(token)
                .when()
                .delete(BASE_URL + ENDPOINT + "/" + id)
                .then()
                .extract()
                .response();
    }
}
