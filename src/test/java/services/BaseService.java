package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseService {

    public static final String BASE_URL = resolverBaseUrl();

    protected final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private static String resolverBaseUrl() {
        String url = System.getProperty("baseUrl");
        if (url == null || url.isBlank()) {
            url = System.getenv("BASE_URL");
        }
        if (url == null || url.isBlank()) {
            url = "http://127.0.0.1:8080";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    protected RequestSpecification requisicao(String token) {
        RequestSpecification requisicao = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        if (token != null) {
            requisicao.header("Authorization", "Bearer " + token);
        }
        return requisicao;
    }

    public <T> T converter(Response resposta, Class<T> tipo) {
        return gson.fromJson(resposta.getBody().asString(), tipo);
    }
}
