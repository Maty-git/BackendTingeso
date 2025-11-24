# BackendTingeso - Sistema de Gestión de Alquiler de Herramientas (ToolRent)

Backend REST API desarrollado con Spring Boot para la gestión integral de un sistema de alquiler de herramientas. El sistema permite gestionar herramientas, préstamos, clientes, deudas y control de inventario mediante Kardex.

## 📋 Descripción del Proyecto

Este sistema backend proporciona una API REST completa para la gestión de un negocio de alquiler de herramientas, incluyendo:

- **Gestión de Herramientas**: Registro, actualización, eliminación y seguimiento de herramientas con diferentes categorías y estados
- **Gestión de Préstamos**: Control de préstamos de herramientas con seguimiento de fechas, estados y multas por atraso
- **Gestión de Clientes**: Administración de clientes y sus datos
- **Gestión de Deudas**: Control de deudas generadas por multas, reparaciones y otros conceptos
- **Sistema Kardex**: Control de inventario y movimientos de herramientas
- **Reportes y Análisis**: Rankings de herramientas más prestadas, préstamos activos, deudas pendientes, etc.

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.4.9**
- **Spring Data JPA**
- **PostgreSQL** (Base de datos de producción)
- **Lombok** (Reducción de código boilerplate)
- **Maven** (Gestión de dependencias)
- **JaCoCo** (Cobertura de código en pruebas)

## 📦 Estructura del Proyecto

```
BackendTingeso/
├── src/
│   ├── main/java/com/ProyectoTingeso1/BackendProyecto1/
│   │   ├── Controllers/      # Controladores REST (5 archivos)
│   │   ├── Services/         # Lógica de negocio (6 archivos)
│   │   ├── Repositories/     # Repositorios JPA (5 archivos)
│   │   ├── Entities/         # Entidades JPA (5 archivos)
│   │   └── DTOs/             # Objetos de transferencia de datos (9 archivos)
│   └── resources/
│       └── application.properties
├── src/test/                 # Pruebas unitarias con ≥90% de cobertura
│   ├── ControllerTests/      # Pruebas de controladores
│   ├── ServiceTests/         # Pruebas de servicios
│   ├── RepositoryTests/      # Pruebas de repositorios
│   ├── EntityTests/          # Pruebas de entidades
│   └── DTOTests/             # Pruebas de DTOs
├── pom.xml                   # Configuración Maven
└── Dockerfile                # Configuración Docker
```

## 🚀 Requisitos Previos

- **Java 21** o superior
- **Maven 3.6+** o usar el wrapper incluido (`mvnw`)
- **PostgreSQL** (para ejecución en producción)
- **IDE** compatible con Java (IntelliJ IDEA, Eclipse, VS Code, etc.)

## ⚙️ Configuración

### Variables de Entorno

El proyecto requiere las siguientes variables de entorno para conectarse a la base de datos:

```bash
DB_URL=jdbc:postgresql://localhost:5432/toolrent
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
```

### Configuración de Base de Datos

1. Crea una base de datos PostgreSQL:
```sql
CREATE DATABASE toolrent;
```

2. La aplicación creará automáticamente las tablas gracias a la configuración `spring.jpa.hibernate.ddl-auto=update`

## 🏃 Ejecución del Proyecto

### Opción 1: Usando Maven Wrapper (Recomendado)

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Opción 2: Usando Maven instalado

```bash
mvn spring-boot:run
```

### Opción 3: Compilar y ejecutar JAR

```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/BackendProyecto1-0.0.1-SNAPSHOT.jar
```

### Opción 4: Usando Docker

```bash
# Construir imagen
docker build -t backend-tingeso .

# Ejecutar contenedor
docker run -p 8090:8090 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/toolrent \
  -e DB_USERNAME=tu_usuario \
  -e DB_PASSWORD=tu_contraseña \
  backend-tingeso
```

## 🌐 Endpoints de la API

### Herramientas (`/api/tools`)
- `POST /api/tools/{rutUser}` - Crear nueva herramienta
- `GET /api/tools/all` - Obtener todas las herramientas con Kardex
- `GET /api/tools/tools` - Obtener todas las herramientas sin Kardex
- `GET /api/tools/{name}/{category}` - Buscar herramienta por nombre y categoría
- `GET /api/tools/for-repair` - Obtener herramientas que requieren reparación
- `PUT /api/tools/{id}/{rutUser}` - Eliminar herramienta (soft delete)
- `PUT /api/tools/repair/{id}/{rutUser}` - Reparar herramienta sin deuda
- `PUT /api/tools/repairDebt/{id}/{rutUser}` - Reparar herramienta con deuda
- `PUT /api/tools/update/{id}/{rutUser}` - Actualizar herramienta

### Préstamos (`/api/loans`)
- `POST /api/loans/save` - Crear nuevo préstamo
- `GET /api/loans/all` - Obtener todos los préstamos
- `GET /api/loans/{id}` - Obtener préstamo por ID
- `GET /api/loans/active` - Obtener préstamos activos con estado
- `GET /api/loans/active/range` - Obtener préstamos activos por rango de fechas
- `GET /api/loans/ranking` - Ranking de herramientas más prestadas
- `GET /api/loans/ranking/range` - Ranking por rango de fechas
- `PUT /api/loans/return/{id}/{userRut}/{bool}` - Devolver préstamo

### Clientes (`/api/clients`)
- `POST /api/clients/save` - Crear nuevo cliente
- `GET /api/clients/all` - Obtener todos los clientes
- `GET /api/clients/rutsClients` - Obtener lista de RUTs de clientes
- `PUT /api/clients/update` - Actualizar cliente
- `DELETE /api/clients/{id}` - Eliminar cliente

### Deudas (`/api/Debts`)
- `POST /api/Debts/save` - Crear nueva deuda
- `GET /api/Debts/all` - Obtener todas las deudas
- `GET /api/Debts/{id}` - Obtener deuda por ID
- `GET /api/Debts/unpaid` - Obtener resumen de deudas no pagadas
- `GET /api/Debts/late` - Obtener clientes con deudas por atraso
- `GET /api/Debts/late/range` - Obtener clientes con deudas por atraso en rango de fechas
- `PUT /api/Debts/pay/{id}` - Pagar deuda

### Kardex (`/api/kardex`)
- Endpoints para gestión de movimientos de inventario (consultar código fuente)

## 🧪 Ejecución de Pruebas

El proyecto incluye una suite completa de pruebas unitarias con cobertura ≥90%.

### Ejecutar todas las pruebas

**Windows:**
```bash
.\mvnw.cmd clean test
```

**Linux/Mac:**
```bash
./mvnw clean test
```

### Ejecutar pruebas con reporte de cobertura

**Windows:**
```bash
.\mvnw.cmd clean test jacoco:report
start target\site\jacoco\index.html
```

**Linux/Mac:**
```bash
./mvnw clean test jacoco:report
open target/site/jacoco/index.html
```

### Usar script automatizado

**Windows:**
```bash
run-tests.bat
```

**Linux/Mac:**
```bash
./run-tests.sh
```

Para más información sobre las pruebas, consulta:
- `INSTRUCCIONES_EJECUTAR_PRUEBAS.md` - Guía detallada
- `GUIA_RAPIDA.md` - Guía rápida
- `TEST_README.md` - Documentación completa

## 📊 Estadísticas del Proyecto

- **18 clases de prueba** con aproximadamente **210 métodos de prueba**
- **Cobertura de código ≥90%** en todas las capas
- **5 Controladores REST**
- **6 Servicios de negocio**
- **5 Repositorios JPA**
- **5 Entidades principales**
- **9 DTOs** para transferencia de datos

## 🎯 Características Principales

- ✅ API REST completa con CORS habilitado
- ✅ Gestión de inventario con Kardex
- ✅ Sistema de préstamos con seguimiento de fechas y multas
- ✅ Gestión de deudas y pagos
- ✅ Reportes y rankings
- ✅ Tareas programadas (scheduling) para automatización
- ✅ Validaciones de negocio
- ✅ Pruebas unitarias exhaustivas
- ✅ Cobertura de código ≥90%

## 📝 Notas Adicionales

- El puerto por defecto es **8090**
- La aplicación utiliza **scheduling** para tareas programadas (para actualización de estados de préstamos)
- Las pruebas utilizan **H2 Database** en memoria, no requiere PostgreSQL
- El proyecto está configurado para desarrollo con **Lombok**

## 📄 Licencia

Consultar archivo `LICENSE` para más información.

## 👥 Autores:
-Matias Ramirez Escobar

Proyecto desarrollado para Tingeso - Backend de ToolRent

---

**¡Listo para usar! 🚀**
