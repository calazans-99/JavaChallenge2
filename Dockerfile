# === Build ===
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Perfil do Maven para empacotar o conector correto no JAR (default: mysql)
ARG BUILD_PROFILE=mysql
ENV BUILD_PROFILE=${BUILD_PROFILE}

COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests -P${BUILD_PROFILE} clean package

# === Runtime ===
FROM eclipse-temurin:17-jre
WORKDIR /app

# Fuso horário (opcional)
ENV TZ=America/Sao_Paulo

# Perfil do Spring usado no runtime (pode sobrescrever com -e SPRING_PROFILES_ACTIVE=...)
ENV SPRING_PROFILES_ACTIVE=mysql

# URL padrão para Compose (serviço "db"); credenciais passam por variável em runtime
ENV SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/challenge?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

# Porta da aplicação
ENV SERVER_PORT=8080

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
