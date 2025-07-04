

# Étape 1 : Build avec Maven et Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Copie uniquement le module courant (ex: discovery-service) + pom parent
COPY pom.xml pom.xml
COPY src src

# Build du microservice uniquement
RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Création d’un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Répertoire de travail dans le conteneur
WORKDIR /app

# Copie du JAR compilé depuis l'étape de build
COPY --from=build /build/target/*.jar app.jar

# Exposition du port (à adapter selon le microservice)
EXPOSE 8080

# Lancement de l'application avec le profil docker
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
