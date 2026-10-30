# Imagem que executa os testes BDD (Cucumber + Rest Assured) com o Maven.
# Usada pelo serviço "testes" do docker-compose.yml.
FROM maven:3.9-eclipse-temurin-21

WORKDIR /testes

# Dependências primeiro: enquanto o pom.xml não mudar, esta camada fica em cache.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Depois os testes (features, schemas e código Java).
COPY src ./src

# "test" (sem "clean"): a pasta target é compartilhada com o computador,
# para que o relatório fique disponível fora do container.
ENTRYPOINT ["mvn", "-B", "test"]
