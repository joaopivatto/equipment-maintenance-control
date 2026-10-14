# ---------- Estagio 1: build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia so o necessario para baixar as dependencias (aproveita o cache do Docker)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

# Copia o codigo e gera o jar (testes rodam no CI, nao aqui)
COPY src/ src/
RUN ./mvnw -B -q -DskipTests package \
    && cp target/*.jar app.jar

# ---------- Estagio 2: execucao ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usuario sem privilegios
RUN useradd --system --no-create-home appuser
COPY --from=build /app/app.jar app.jar
USER appuser

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]