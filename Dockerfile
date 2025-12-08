# Opção 1: Usar Eclipse Temurin (recomendado)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar o JAR da aplicação
COPY target/*.jar app.jar

# Expor a porta
EXPOSE 8082

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]