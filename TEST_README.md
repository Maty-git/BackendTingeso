# Guía de Pruebas Unitarias - BackendProyecto1

Esta guía te ayudará a ejecutar y comprender las pruebas unitarias del proyecto BackendProyecto1.

## 📋 Contenido

- [Estructura de Pruebas](#estructura-de-pruebas)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Ejecutar las Pruebas](#ejecutar-las-pruebas)
- [Ver Reporte de Cobertura](#ver-reporte-de-cobertura)
- [Descripción de las Pruebas](#descripción-de-las-pruebas)

## 📁 Estructura de Pruebas

```
src/test/java/com/ProyectoTingeso1/BackendProyecto1/
├── RepositoryTests/
│   ├── ClientRepositoryTest.java
│   ├── DebtRepositoryTest.java
│   ├── KardexRepositoryTest.java
│   ├── LoanRepositoryTest.java
│   └── ToolRepositoryTest.java
├── ServiceTests/
│   ├── ClientServiceTest.java
│   ├── DebtServiceTest.java
│   ├── KardexServiceTest.java
│   ├── LoanServiceTest.java
│   ├── LoanSchedulerServiceTest.java
│   └── ToolServiceTest.java
├── ControllerTests/
│   ├── ClientControllerTest.java
│   ├── DebtControllerTest.java
│   ├── KardexControllerTest.java
│   ├── LoanControllerTest.java
│   └── ToolControllerTest.java
└── EntityTests/
    └── EntityTest.java
```

## 🛠 Tecnologías Utilizadas

- **JUnit 5**: Framework de testing principal
- **AssertJ**: Librería para aserciones fluidas y legibles
- **Mockito**: Framework para crear mocks y simular dependencias
- **Spring Boot Test**: Utilidades de testing de Spring Boot
- **MockMvc**: Para pruebas de controladores REST
- **JaCoCo**: Herramienta de cobertura de código

## ▶️ Ejecutar las Pruebas

### Opción 1: Usando Maven (Recomendado)

#### Windows (PowerShell/CMD):

```bash
# Ejecutar todas las pruebas
.\mvnw.cmd test

# Ejecutar pruebas con reporte de cobertura
.\mvnw.cmd clean test

# Ejecutar y generar reporte HTML de cobertura
.\mvnw.cmd clean test jacoco:report
```

#### Linux/Mac:

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar pruebas con reporte de cobertura
./mvnw clean test

# Ejecutar y generar reporte HTML de cobertura
./mvnw clean test jacoco:report
```

### Opción 2: Usando Maven instalado globalmente

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar con cobertura
mvn clean test jacoco:report
```

### Opción 3: Ejecutar pruebas específicas

```bash
# Ejecutar solo pruebas de Repositories
.\mvnw.cmd test -Dtest="*RepositoryTest"

# Ejecutar solo pruebas de Services
.\mvnw.cmd test -Dtest="*ServiceTest"

# Ejecutar solo pruebas de Controllers
.\mvnw.cmd test -Dtest="*ControllerTest"

# Ejecutar una clase de prueba específica
.\mvnw.cmd test -Dtest=ClientServiceTest

# Ejecutar un método de prueba específico
.\mvnw.cmd test -Dtest=ClientServiceTest#whenSaveClient_thenClientIsSaved
```

### Opción 4: Desde tu IDE

#### IntelliJ IDEA:
1. Haz clic derecho en la carpeta `src/test/java`
2. Selecciona "Run 'All Tests'"

#### Eclipse:
1. Haz clic derecho en la carpeta `src/test/java`
2. Selecciona "Run As" > "JUnit Test"

#### Visual Studio Code:
1. Instala la extensión "Test Runner for Java"
2. Haz clic en el ícono de testing en la barra lateral
3. Ejecuta todas las pruebas

## 📊 Ver Reporte de Cobertura

Después de ejecutar las pruebas con JaCoCo, puedes ver el reporte de cobertura:

### Ubicación del reporte:
```
target/site/jacoco/index.html
```

### Abrir el reporte:

#### Windows:
```bash
start target/site/jacoco/index.html
```

#### Linux:
```bash
xdg-open target/site/jacoco/index.html
```

#### Mac:
```bash
open target/site/jacoco/index.html
```

### Interpretación del reporte:

El reporte de JaCoCo muestra:
- **Verde**: Código completamente cubierto por pruebas
- **Amarillo**: Código parcialmente cubierto
- **Rojo**: Código no cubierto

Métricas principales:
- **Instrucciones**: Líneas de bytecode ejecutadas
- **Ramas**: Cobertura de decisiones (if, switch, etc.)
- **Líneas**: Líneas de código fuente cubiertas
- **Métodos**: Métodos que tienen al menos una prueba
- **Clases**: Clases que tienen al menos una prueba

## 📝 Descripción de las Pruebas

### 1. Pruebas de Repositories (`@DataJpaTest`)

Prueban las consultas personalizadas y operaciones de base de datos:

- **ClientRepositoryTest**: 
  - ✅ Búsqueda de clientes por RUT
  - ✅ Obtención de todos los RUTs
  - ✅ Consultas personalizadas para DTOs

- **ToolRepositoryTest**:
  - ✅ Resumen de herramientas agrupadas
  - ✅ Búsqueda de herramientas por estado
  - ✅ Filtros por nombre y categoría

- **LoanRepositoryTest**:
  - ✅ Consultas de préstamos activos
  - ✅ Estadísticas y rankings
  - ✅ Filtros por rango de fechas

- **DebtRepositoryTest**:
  - ✅ Búsqueda de deudas impagas
  - ✅ Resúmenes de deudas por cliente
  - ✅ Filtros por tipo de deuda

- **KardexRepositoryTest**:
  - ✅ Historial de movimientos por herramienta
  - ✅ Filtros por rango de fechas

### 2. Pruebas de Services (`@ExtendWith(MockitoExtension.class)`)

Prueban la lógica de negocio usando mocks:

- **ClientServiceTest**:
  - ✅ CRUD completo de clientes
  - ✅ Validaciones de negocio
  - ✅ Manejo de excepciones

- **LoanServiceTest**:
  - ✅ Creación de préstamos con validaciones
  - ✅ Devolución de herramientas
  - ✅ Manejo de daños y deudas
  - ✅ Restricciones de negocio (máx. 5 préstamos, cliente restringido)

- **ToolServiceTest**:
  - ✅ Creación masiva de herramientas
  - ✅ Reparaciones con y sin deuda
  - ✅ Baja de herramientas
  - ✅ Actualizaciones por grupo

- **DebtServiceTest**:
  - ✅ Creación y pago de deudas
  - ✅ Actualización de estado del cliente
  - ✅ Filtros y resúmenes

- **LoanSchedulerServiceTest**:
  - ✅ Detección de préstamos atrasados
  - ✅ Cálculo automático de multas
  - ✅ Actualización de estados

- **KardexServiceTest**:
  - ✅ Registro de movimientos
  - ✅ Consultas de historial

### 3. Pruebas de Controllers (`@WebMvcTest`)

Prueban los endpoints REST usando MockMvc:

- **ClientControllerTest**:
  - ✅ POST `/api/clients/save`
  - ✅ GET `/api/clients/all`
  - ✅ GET `/api/clients/rutsClients`
  - ✅ PUT `/api/clients/update`
  - ✅ DELETE `/api/clients/{id}`

- **LoanControllerTest**:
  - ✅ POST `/api/loans/save`
  - ✅ GET `/api/loans/all`
  - ✅ PUT `/api/loans/return/{id}/{userRut}/{bool}`
  - ✅ GET `/api/loans/active`
  - ✅ GET `/api/loans/ranking`

- **ToolControllerTest**:
  - ✅ POST `/api/tools/{rutUser}`
  - ✅ GET `/api/tools/all`
  - ✅ PUT `/api/tools/{id}/{rutUser}`
  - ✅ PUT `/api/tools/repair/{id}/{rutUser}`
  - ✅ GET `/api/tools/for-repair`

- **DebtControllerTest**:
  - ✅ POST `/api/Debts/save`
  - ✅ GET `/api/Debts/all`
  - ✅ PUT `/api/Debts/pay/{id}`
  - ✅ GET `/api/Debts/unpaid`
  - ✅ GET `/api/Debts/late`

- **KardexControllerTest**:
  - ✅ GET `/api/kardex/all`
  - ✅ GET `/api/kardex/tool/{toolId}`
  - ✅ GET `/api/kardex/tool/{toolId}/range`

### 4. Pruebas de Entidades

Prueban los modelos de datos y sus métodos Lombok:

- ✅ Getters y Setters
- ✅ Constructores
- ✅ Equals y HashCode
- ✅ ToString
- ✅ Enumeraciones

## 🎯 Objetivo de Cobertura

**Meta**: ≥ 90% de cobertura de líneas de código

### Áreas cubiertas:

| Componente | Cobertura Esperada |
|------------|-------------------|
| Repositories | 95-100% |
| Services | 90-95% |
| Controllers | 90-95% |
| Entities | 95-100% |
| **TOTAL** | **≥ 90%** |

## 🔍 Verificar Cobertura por Componente

```bash
# Generar reporte y abrirlo
.\mvnw.cmd clean test jacoco:report
start target/site/jacoco/index.html

# En el reporte, navega a:
# - com.ProyectoTingeso1.BackendProyecto1.Repositories
# - com.ProyectoTingeso1.BackendProyecto1.Services
# - com.ProyectoTingeso1.BackendProyecto1.Controllers
# - com.ProyectoTingeso1.BackendProyecto1.Entities
```

## 🐛 Solución de Problemas

### Error: "No se puede encontrar mvnw"
```bash
# Asegúrate de estar en el directorio raíz del proyecto
cd BackendTingeso

# Verifica que exista el archivo
dir mvnw.cmd  # Windows
ls mvnw       # Linux/Mac
```

### Error: "Tests no se ejecutan"
```bash
# Limpia el proyecto y vuelve a compilar
.\mvnw.cmd clean install
```

### Error: "Falla de conexión a base de datos"
Las pruebas de Repository usan una base de datos en memoria H2, no necesitan PostgreSQL corriendo.

### Ver logs detallados de las pruebas
```bash
.\mvnw.cmd test -X
```

## 📚 Recursos Adicionales

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)

## ✅ Checklist de Pruebas

- [x] Pruebas de Repositories (5 clases)
- [x] Pruebas de Services (6 clases)
- [x] Pruebas de Controllers (5 clases)
- [x] Pruebas de Entidades (1 clase)
- [x] Configuración de JaCoCo
- [x] Cobertura ≥ 90%

## 🎓 Buenas Prácticas Implementadas

✅ **Patrón AAA (Arrange-Act-Assert)**: Todas las pruebas siguen este patrón
✅ **Nombres descriptivos**: Los nombres de las pruebas describen claramente qué se está probando
✅ **Aislamiento**: Cada prueba es independiente
✅ **Mocks apropiados**: Se usan mocks para aislar las dependencias
✅ **Cobertura completa**: Se prueban casos exitosos, casos de error y casos límite
✅ **Fast Tests**: Las pruebas se ejecutan rápidamente usando bases de datos en memoria

---

**¡Buena suerte con tus pruebas! 🚀**

