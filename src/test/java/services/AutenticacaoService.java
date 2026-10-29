package services;

import io.restassured.response.Response;
import model.LoginModel;
import model.TokenModel;
import model.UsuarioModel;

import java.util.UUID;

public class AutenticacaoService extends BaseService {

    public static final String SENHA_PADRAO = "Senha123";

    /** Cria os dados de um usuário com e-mail único (ex.: teste.3f9a2c1b@esg.com).
     * Assim cada cenário pode rodar sem receber "e-mail já cadastrado". */
    public UsuarioModel novoUsuario(String perfil) {
        String identificador = UUID.randomUUID().toString().substring(0, 8);
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome("Usuário Teste " + identificador);
        usuario.setEmail("teste." + identificador + "@esg.com");
        usuario.setSenha(SENHA_PADRAO);
        usuario.setRole(perfil);
        return usuario;
    }

    public Response cadastrar(UsuarioModel usuario) {
        return requisicao(null)
                .body(gson.toJson(usuario))
                .when()
                .post(BASE_URL + "/auth/register")
                .then()
                .extract()
                .response();
    }

    public Response login(String email, String senha) {
        LoginModel login = new LoginModel();
        login.setEmail(email);
        login.setSenha(senha);
        return requisicao(null)
                .body(gson.toJson(login))
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .extract()
                .response();
    }

    public String autenticarComo(String perfil) {
        UsuarioModel usuario = novoUsuario(perfil);

        Response cadastro = cadastrar(usuario);
        if (cadastro.statusCode() != 200) {
            throw new IllegalStateException("Falha ao preparar usuário " + perfil
                    + ": status " + cadastro.statusCode() + " - " + cadastro.asString());
        }

        Response login = login(usuario.getEmail(), usuario.getSenha());
        if (login.statusCode() != 200) {
            throw new IllegalStateException("Falha no login do usuário " + perfil
                    + ": status " + login.statusCode() + " - " + login.asString());
        }

        return converter(login, TokenModel.class).getToken();
    }
}
