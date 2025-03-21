# ./dockerfile
# Etapa de compilación
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Usa el puerto desde variables de entorno
ENV PORT=${PORT:-8090}
EXPOSE ${PORT}
# Usa el comando sin hardcodear credenciales
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dserver.port=${PORT}", "-jar", "/app/app.jar"]