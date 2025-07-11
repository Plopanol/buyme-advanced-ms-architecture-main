

# Étape 1 : Build avec Maven et Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Copie uniquement le module courant (ex: discovery-service) + pom parent
COPY product-service/pom.xml pom.xml
COPY product-service/src src

# Build du microservice uniquement
RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Création d’un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Répertoire de travail dans le conteneur
WORKDIR /app

COPY keyssh/server.crt server.crt
RUN keytool -import -trustcacerts -alias keycloak-cert -file server.crt -keystore /app/truststore.jks -storepass changeit -noprompt

# Copie du JAR compilé depuis l'étape de build
COPY --from=build /build/target/*.jar app.jar

# Exposition du port (à adapter selon le microservice)
EXPOSE 8082

# Lancement de l'application avec le profil docker
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Djavax.net.ssl.trustStore=/app/truststore.jks", "-Djavax.net.ssl.trustStorePassword=changeit", "-jar", "app.jar"]
