package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import org.junit.Assert;
import services.ContextoCenario;
import services.ContratoService;

import java.io.IOException;
import java.util.Set;

public class ValidacaoSteps {

    private final ContratoService contratoService = new ContratoService();

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusEsperado) {
        Assert.assertEquals("Status code inesperado. Corpo da resposta: "
                        + ContextoCenario.resposta.asString(),
                statusEsperado, ContextoCenario.resposta.statusCode());
    }

    @E("o campo {string} do corpo da resposta deve ser {string}")
    public void oCampoDoCorpoDaRespostaDeveSer(String campo, String valorEsperado) {
        String valorRecebido = ContextoCenario.resposta.jsonPath().getString(campo);
        Assert.assertEquals("Valor do campo \"" + campo + "\" na resposta", valorEsperado, valorRecebido);
    }

    @E("a resposta deve estar em conformidade com o contrato {string}")
    public void aRespostaDeveEstarEmConformidadeComOContrato(String contrato) throws IOException {
        Set<ValidationMessage> violacoes = contratoService.validar(ContextoCenario.resposta, contrato);
        Assert.assertTrue("A resposta viola o contrato \"" + contrato + "\": " + violacoes, violacoes.isEmpty());
    }
}
