# Define o Maven com jdk 21 para build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia as configurações do Maven e o código fonte
COPY pom.xml .
COPY src ./src

# Compila o projeto e gera o arquivo .jar
RUN mvn clean package -DskipTests

# Usa o jre para executar o app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o .jar gerado na pasta target
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Executa o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]