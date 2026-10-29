package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import model.IniciativaModel;
import org.junit.Assert;
import services.AutenticacaoService;
import services.ContextoCenario;
import services.IniciativaService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class IniciativaSteps {

    private final IniciativaService iniciativaService = new IniciativaService();
    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    // ---------------------------------------------------------------- preparação

    @E("que eu tenha os seguintes dados da iniciativa:")
    public void queEuTenhaOsSeguintesDadosDaIniciativa(DataTable tabela) {
        ContextoCenario.iniciativa = new IniciativaModel();
        aplicarTabela(ContextoCenario.iniciativa, tabela);
    }

    @E("que eu tenha os dados de uma iniciativa válida")
    public void queEuTenhaOsDadosDeUmaIniciativaValida() {
        ContextoCenario.iniciativa = iniciativaService.iniciativaValida();
    }

    @E("o campo {string} da iniciativa tenha o valor {string}")
    public void oCampoDaIniciativaTenhaOValor(String campo, String valor) {
        iniciativaService.definirCampo(ContextoCenario.iniciativa, campo, valor);
    }

    @E("que exista uma iniciativa cadastrada")
    public void queExistaUmaIniciativaCadastrada() {
        ContextoCenario.iniciativaCadastrada = cadastrarIniciativaComoAdmin();
    }

    @Dado("que exista uma iniciativa que já foi excluída")
    public void queExistaUmaIniciativaQueJaFoiExcluida() {
        IniciativaModel iniciativa = cadastrarIniciativaComoAdmin();
        Response exclusao = iniciativaService.excluir(iniciativa.getReducaoCarbonoId(), tokenAdmin());
        Assert.assertEquals("Pré-condição: a exclusão deveria funcionar", 204, exclusao.statusCode());
        ContextoCenario.idIniciativaExcluida = iniciativa.getReducaoCarbonoId();
    }

    // ---------------------------------------------------------------- ações

    @Quando("eu enviar a requisição de cadastro de iniciativa")
    public void euEnviarARequisicaoDeCadastroDeIniciativa() {
        ContextoCenario.resposta = iniciativaService.cadastrar(ContextoCenario.iniciativa, ContextoCenario.token);
    }

    @Quando("eu buscar todas as iniciativas")
    public void euBuscarTodasAsIniciativas() {
        ContextoCenario.resposta = iniciativaService.listar(ContextoCenario.token);
    }

    @Quando("eu buscar a iniciativa cadastrada pelo seu ID")
    public void euBuscarAIniciativaCadastradaPeloSeuId() {
        ContextoCenario.resposta = iniciativaService.buscarPorId(
                ContextoCenario.iniciativaCadastrada.getReducaoCarbonoId(), ContextoCenario.token);
    }

    @Quando("eu buscar a iniciativa excluída pelo seu ID")
    public void euBuscarAIniciativaExcluidaPeloSeuId() {
        ContextoCenario.resposta = iniciativaService.buscarPorId(
                ContextoCenario.idIniciativaExcluida, ContextoCenario.token);
    }

    @Quando("eu atualizar a iniciativa cadastrada com os seguintes dados:")
    public void euAtualizarAIniciativaCadastradaComOsSeguintesDados(DataTable tabela) {
        IniciativaModel atualizada = iniciativaService.copiar(ContextoCenario.iniciativaCadastrada);
        aplicarTabela(atualizada, tabela);
        ContextoCenario.iniciativaAtualizada = atualizada;
        ContextoCenario.resposta = iniciativaService.atualizar(atualizada, ContextoCenario.token);
    }

    @Quando("eu tentar atualizar a iniciativa excluída")
    public void euTentarAtualizarAIniciativaExcluida() {
        IniciativaModel iniciativa = iniciativaService.iniciativaValida();
        iniciativa.setReducaoCarbonoId(ContextoCenario.idIniciativaExcluida);
        ContextoCenario.resposta = iniciativaService.atualizar(iniciativa, ContextoCenario.token);
    }

    @Quando("eu excluir a iniciativa cadastrada")
    public void euExcluirAIniciativaCadastrada() {
        ContextoCenario.resposta = iniciativaService.excluir(
                ContextoCenario.iniciativaCadastrada.getReducaoCarbonoId(), ContextoCenario.token);
    }

    @Quando("eu excluir novamente a iniciativa excluída")
    public void euExcluirNovamenteAIniciativaExcluida() {
        ContextoCenario.resposta = iniciativaService.excluir(
                ContextoCenario.idIniciativaExcluida, ContextoCenario.token);
    }

    // ---------------------------------------------------------------- validações

    @Então("o corpo da resposta deve conter os dados da iniciativa enviada")
    public void oCorpoDaRespostaDeveConterOsDadosDaIniciativaEnviada() {
        IniciativaModel recebida = iniciativaService.converter(ContextoCenario.resposta, IniciativaModel.class);
        Assert.assertNotNull("A iniciativa cadastrada deveria receber um ID", recebida.getReducaoCarbonoId());
        compararDados(ContextoCenario.iniciativa, recebida);
    }

    @Então("o corpo da resposta deve conter os dados da iniciativa cadastrada")
    public void oCorpoDaRespostaDeveConterOsDadosDaIniciativaCadastrada() {
        IniciativaModel recebida = iniciativaService.converter(ContextoCenario.resposta, IniciativaModel.class);
        Assert.assertEquals(ContextoCenario.iniciativaCadastrada.getReducaoCarbonoId(), recebida.getReducaoCarbonoId());
        compararDados(ContextoCenario.iniciativaCadastrada, recebida);
    }

    @Então("o corpo da resposta deve refletir os dados atualizados")
    public void oCorpoDaRespostaDeveRefletirOsDadosAtualizados() {
        IniciativaModel recebida = iniciativaService.converter(ContextoCenario.resposta, IniciativaModel.class);
        Assert.assertEquals(ContextoCenario.iniciativaAtualizada.getReducaoCarbonoId(), recebida.getReducaoCarbonoId());
        compararDados(ContextoCenario.iniciativaAtualizada, recebida);
    }

    @Então("a lista de iniciativas deve conter a iniciativa cadastrada")
    public void aListaDeIniciativasDeveConterAIniciativaCadastrada() {
        IniciativaModel[] lista = iniciativaService.converter(ContextoCenario.resposta, IniciativaModel[].class);
        Long idEsperado = ContextoCenario.iniciativaCadastrada.getReducaoCarbonoId();

        boolean encontrada = Arrays.stream(lista)
                .anyMatch(item -> idEsperado.equals(item.getReducaoCarbonoId()));
        Assert.assertTrue("A iniciativa de ID " + idEsperado + " deveria estar na lista", encontrada);
    }

    @Então("a iniciativa excluída não deve mais ser encontrada")
    public void aIniciativaExcluidaNaoDeveMaisSerEncontrada() {
        Response busca = iniciativaService.buscarPorId(
                ContextoCenario.iniciativaCadastrada.getReducaoCarbonoId(), tokenAdmin());
        Assert.assertEquals("A iniciativa excluída não deveria mais existir", 404, busca.statusCode());
    }

    // ---------------------------------------------------------------- apoio

    /** Token de administrador para preparar dados (reaproveita o do cenário, se houver). */
    private String tokenAdmin() {
        if (ContextoCenario.tokenAdmin == null) {
            ContextoCenario.tokenAdmin = autenticacaoService.autenticarComo("ADMIN");
        }
        return ContextoCenario.tokenAdmin;
    }

    private IniciativaModel cadastrarIniciativaComoAdmin() {
        Response cadastro = iniciativaService.cadastrar(iniciativaService.iniciativaValida(), tokenAdmin());
        Assert.assertEquals("Pré-condição: o cadastro da iniciativa deveria funcionar. Corpo: "
                + cadastro.asString(), 201, cadastro.statusCode());
        return iniciativaService.converter(cadastro, IniciativaModel.class);
    }

    private void aplicarTabela(IniciativaModel iniciativa, DataTable tabela) {
        for (Map<String, String> linha : tabela.asMaps()) {
            iniciativaService.definirCampo(iniciativa, linha.get("campo"), linha.get("valor"));
        }
    }

    private void compararDados(IniciativaModel esperada, IniciativaModel recebida) {
        Assert.assertEquals(esperada.getDescricao(), recebida.getDescricao());
        Assert.assertEquals(esperada.getCategoria(), recebida.getCategoria());
        Assert.assertEquals(esperada.getStatus(), recebida.getStatus());
        compararValor("emissaoBaseTco2E", esperada.getEmissaoBaseTco2E(), recebida.getEmissaoBaseTco2E());
        compararValor("metaReducaoTco2E", esperada.getMetaReducaoTco2E(), recebida.getMetaReducaoTco2E());
    }

    /** Compara números pelo valor: 5200.00 e 5200 são iguais. */
    private void compararValor(String campo, BigDecimal esperado, BigDecimal recebido) {
        Assert.assertNotNull("O campo " + campo + " deveria vir preenchido", recebido);
        Assert.assertEquals("Valor divergente no campo " + campo + ": esperado " + esperado + ", recebido " + recebido,
                0, esperado.compareTo(recebido));
    }
}
