package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ContratoService {

    private final ObjectMapper mapper = new ObjectMapper();

    public Set<ValidationMessage> validar(Response resposta, String contrato) throws IOException {
        String caminho = "schemas/" + contrato + ".json";

        try (InputStream arquivo = getClass().getClassLoader().getResourceAsStream(caminho)) {
            if (arquivo == null) {
                throw new IllegalArgumentException("Contrato não encontrado: " + caminho);
            }
            JsonSchema schema = JsonSchemaFactory
                    .getInstance(SpecVersion.VersionFlag.V7)
                    .getSchema(arquivo);

            JsonNode corpo = mapper.readTree(resposta.getBody().asString());
            return schema.validate(corpo);
        }
    }
}
