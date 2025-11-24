# Imagen base con JDK
FROM openjdk:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar generado dentro del contenedor
COPY target/BackendProyecto1-0.0.1-SNAPSHOT.jar app.jar

# Puerto en el que corre Spring Boot
EXPOSE 8090

# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
