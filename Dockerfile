# ===============================
# ETAPA 1: CONSTRUCCIÓN (Build)
# ===============================
# Usamos una imagen de Maven con Java 21 para compilar
FROM maven:3.9-eclipse-temurin-21 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos todo el código fuente al contenedor
COPY . .

# Ejecutamos Maven para construir el .jar (saltando los tests para ir más rápido)
RUN mvn clean package -DskipTests

# Imagen base con JDK
FROM eclipse-temurin:21-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar generado dentro del contenedor
COPY --from=builder /app/target/BackendProyecto1-0.0.1-SNAPSHOT.jar app.jar

# Puerto en el que corre Spring Boot
EXPOSE 8090

# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
