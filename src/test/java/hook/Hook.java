package hook;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import services.BaseService;
import services.ContextoCenario;

import static io.restassured.RestAssured.given;

public class Hook {

    @BeforeAll
    public static void verificarApiDisponivel() {
        System.out.println(">>> Executando os testes contra: " + BaseService.BASE_URL);
        try {
            given().when().get(BaseService.BASE_URL + "/api/reducao-carbono");
        } catch (Exception e) {
            throw new IllegalStateException("A API não está acessível em " + BaseService.BASE_URL
                    + ". Suba a API com 'docker compose up -d' antes de executar os testes.", e);
        }
    }

    @Before
    public void prepararCenario(Scenario cenario) {
        ContextoCenario.limpar();
        System.out.println(">>> Cenário: " + cenario.getName());
    }

    @After
    public void registrarResposta(Scenario cenario) {
        if (cenario.isFailed() && ContextoCenario.resposta != null) {
            cenario.attach(
                    "Status: " + ContextoCenario.resposta.statusCode() + "\n"
                            + ContextoCenario.resposta.asString(),
                    "text/plain",
                    "Última resposta da API");
        }
    }
}
