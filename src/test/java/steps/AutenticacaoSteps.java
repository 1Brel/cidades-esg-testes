package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import model.TokenModel;
import model.UsuarioModel;
import org.junit.Assert;
import services.AutenticacaoService;
import services.ContextoCenario;

public class AutenticacaoSteps {

    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    @Dado("que eu tenha os dados de um novo usuário com perfil {string}")
    public void queEuTenhaOsDadosDeUmNovoUsuarioComPerfil(String perfil) {
        ContextoCenario.usuario = autenticacaoService.novoUsuario(perfil);
    }

    @E("que esse usuário já esteja cadastrado na API")
    public void queEsseUsuarioJaEstejaCadastradoNaApi() {
        var cadastro = autenticacaoService.cadastrar(ContextoCenario.usuario);
        Assert.assertEquals("Pré-condição: o primeiro cadastro deveria funcionar. Corpo: "
                + cadastro.asString(), 200, cadastro.statusCode());
    }

    @E("que o e-mail desse usuário seja {string}")
    public void queOEmailDesseUsuarioSeja(String email) {
        ContextoCenario.usuario.setEmail(email);
    }

    @Quando("eu enviar a requisição de cadastro de usuário")
    public void euEnviarARequisicaoDeCadastroDeUsuario() {
        ContextoCenario.resposta = autenticacaoService.cadastrar(ContextoCenario.usuario);
    }

    @Então("o corpo da resposta deve conter os dados do usuário cadastrado")
    public void oCorpoDaRespostaDeveConterOsDadosDoUsuarioCadastrado() {
        UsuarioModel enviado = ContextoCenario.usuario;
        UsuarioModel recebido = autenticacaoService.converter(ContextoCenario.resposta, UsuarioModel.class);

        Assert.assertNotNull("O usuário cadastrado deveria receber um ID", recebido.getUsuarioId());
        Assert.assertEquals(enviado.getNome(), recebido.getNome());
        Assert.assertEquals(enviado.getEmail(), recebido.getEmail());
        Assert.assertEquals(enviado.getRole(), recebido.getRole());
    }

    @Dado("que exista um usuário cadastrado com perfil {string}")
    public void queExistaUmUsuarioCadastradoComPerfil(String perfil) {
        ContextoCenario.usuario = autenticacaoService.novoUsuario(perfil);
        var cadastro = autenticacaoService.cadastrar(ContextoCenario.usuario);
        Assert.assertEquals("Pré-condição: o cadastro do usuário deveria funcionar. Corpo: "
                + cadastro.asString(), 200, cadastro.statusCode());
    }

    @Quando("eu realizar o login com as credenciais desse usuário")
    public void euRealizarOLoginComAsCredenciaisDesseUsuario() {
        ContextoCenario.resposta = autenticacaoService.login(
                ContextoCenario.usuario.getEmail(), ContextoCenario.usuario.getSenha());
    }

    @Quando("eu realizar o login com a senha {string}")
    public void euRealizarOLoginComASenha(String senha) {
        ContextoCenario.resposta = autenticacaoService.login(ContextoCenario.usuario.getEmail(), senha);
    }

    @Quando("eu realizar o login com o e-mail {string}")
    public void euRealizarOLoginComOEmail(String email) {
        ContextoCenario.resposta = autenticacaoService.login(email, AutenticacaoService.SENHA_PADRAO);
    }

    @Então("o corpo da resposta deve conter um token de acesso")
    public void oCorpoDaRespostaDeveConterUmTokenDeAcesso() {
        TokenModel token = autenticacaoService.converter(ContextoCenario.resposta, TokenModel.class);

        Assert.assertNotNull("A resposta deveria conter um token", token.getToken());
        Assert.assertEquals("Um token JWT tem três partes separadas por ponto",
                3, token.getToken().split("\\.").length);
    }

    @Dado("que eu esteja autenticado como usuário com perfil {string}")
    public void queEuEstejaAutenticadoComoUsuarioComPerfil(String perfil) {
        ContextoCenario.token = autenticacaoService.autenticarComo(perfil);
        if ("ADMIN".equals(perfil)) {
            ContextoCenario.tokenAdmin = ContextoCenario.token;
        }
    }

    @Dado("que eu acesse a API com a credencial {string}")
    public void queEuAcesseAApiComACredencial(String credencial) {
        switch (credencial) {
            case "sem token" -> ContextoCenario.token = null;
            case "token inválido" -> ContextoCenario.token = "token-invalido";
            case "perfil USER" -> ContextoCenario.token = autenticacaoService.autenticarComo("USER");
            default -> throw new IllegalArgumentException("Credencial inesperada: " + credencial);
        }
    }
}
