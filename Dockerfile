FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar o JAR da aplicação
COPY target/*.jar app.jar

# Expor a porta
EXPOSE 8082

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]