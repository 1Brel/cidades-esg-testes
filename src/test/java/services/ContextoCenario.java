package services;

import io.restassured.response.Response;
import model.IniciativaModel;
import model.UsuarioModel;

public final class ContextoCenario {

    public static Response resposta;
    public static String token;
    public static String tokenAdmin;
    public static UsuarioModel usuario;
    public static IniciativaModel iniciativa;
    public static IniciativaModel iniciativaCadastrada;
    public static IniciativaModel iniciativaAtualizada;
    public static Long idIniciativaExcluida;

    private ContextoCenario() {
    }

    public static void limpar() {
        resposta = null;
        token = null;
        tokenAdmin = null;
        usuario = null;
        iniciativa = null;
        iniciativaCadastrada = null;
        iniciativaAtualizada = null;
        idIniciativaExcluida = null;
    }
}
