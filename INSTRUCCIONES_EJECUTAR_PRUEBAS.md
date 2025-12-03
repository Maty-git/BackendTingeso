# 🚀 Instrucciones para Ejecutar Pruebas Unitarias

## ⚡ Ejecución Rápida

### Opción 1: Usar Script Automatizado (Recomendado)

**Windows:**
```bash
run-tests.bat
```

Este script:
1. Limpia el proyecto
2. Ejecuta todas las pruebas
3. Genera el reporte de cobertura
4. Abre automáticamente el reporte en tu navegador

### Opción 2: Comandos Manuales

**Ejecutar TODAS las pruebas con reporte de cobertura:**
```bash
.\mvnw.cmd clean test jacoco:report
```

**Ver el reporte de cobertura:**
```bash
start target\site\jacoco\index.html
```

## 📊 Comandos Útiles

### Ejecutar Categorías Específicas de Pruebas

```bash
# Solo pruebas de Repositories
.\mvnw.cmd test -Dtest="*RepositoryTest"

# Solo pruebas de Services
.\mvnw.cmd test -Dtest="*ServiceTest"

# Solo pruebas de Controllers
.\mvnw.cmd test -Dtest="*ControllerTest"

# Solo pruebas de Entities
.\mvnw.cmd test -Dtest="EntityTest"
```

### Ejecutar Pruebas Individuales

```bash
# Una clase completa
.\mvnw.cmd test -Dtest=ClientServiceTest

# Un método específico
.\mvnw.cmd test -Dtest=ClientServiceTest#whenSaveClient_thenClientIsSaved
```

### Comandos de Limpieza

```bash
# Limpiar proyecto
.\mvnw.cmd clean

# Limpiar y compilar
.\mvnw.cmd clean compile

# Limpiar, compilar y ejecutar pruebas
.\mvnw.cmd clean test
```

## 📈 Verificar Cobertura

### Paso 1: Ejecutar pruebas con JaCoCo
```bash
.\mvnw.cmd clean test jacoco:report
```

### Paso 2: Abrir reporte
```bash
start target\site\jacoco\index.html
```

### Paso 3: Verificar porcentaje
En el reporte, busca la columna "Cov." (Coverage) que debe mostrar **≥ 90%**

## 🎯 Objetivo de Cobertura

| Componente | Archivos | Objetivo |
|------------|----------|----------|
| Repositories | 5 | ≥ 90% |
| Services | 6 | ≥ 90% |
| Controllers | 5 | ≥ 90% |
| Entities | 5 | ≥ 90% |
| **TOTAL** | **18 clases de prueba** | **≥ 90%** |

## ✅ Checklist de Verificación

Antes de entregar tu proyecto, verifica:

- [ ] Ejecutar `.\mvnw.cmd clean test`
- [ ] Todas las pruebas pasan (color verde)
- [ ] Ejecutar `.\mvnw.cmd jacoco:report`
- [ ] Abrir reporte: `target\site\jacoco\index.html`
- [ ] Verificar cobertura ≥ 90%
- [ ] Revisar que no hay líneas rojas importantes sin cubrir

## 🔧 Solución de Problemas

### Error: "mvnw.cmd no se reconoce"
```bash
# Asegúrate de usar .\ al inicio
.\mvnw.cmd test
```

### Error: "No se pueden ejecutar las pruebas"
```bash
# Limpia el proyecto primero
.\mvnw.cmd clean
# Luego ejecuta las pruebas
.\mvnw.cmd test
```

### Error: "Fallan las pruebas de Repository"
Las pruebas usan H2 (base de datos en memoria), no necesitas PostgreSQL corriendo.

### Ver logs detallados
```bash
.\mvnw.cmd test -X
```

## 📁 Estructura de Archivos de Prueba

```
BackendTingeso/
├── src/
│   ├── main/java/...                    # Código de producción
│   └── test/
│       ├── java/
│       │   └── com/ProyectoTingeso1/BackendProyecto1/
│       │       ├── RepositoryTests/      # 5 archivos
│       │       ├── ServiceTests/         # 6 archivos
│       │       ├── ControllerTests/      # 5 archivos
│       │       ├── EntityTests/          # 1 archivo
│       │       └── DTOTests/             # 1 archivo
│       └── resources/
│           └── application-test.properties
├── pom.xml                               # Configuración Maven + JaCoCo
├── run-tests.bat                         # Script automatizado
├── TEST_README.md                        # Documentación completa
├── TESTING_SUMMARY.md                    # Resumen ejecutivo
└── INSTRUCCIONES_EJECUTAR_PRUEBAS.md    # Este archivo
```

## 📚 Archivos de Documentación

1. **TEST_README.md**: Guía completa y detallada
2. **TESTING_SUMMARY.md**: Resumen ejecutivo del proyecto
3. **INSTRUCCIONES_EJECUTAR_PRUEBAS.md**: Este archivo - guía rápida

## 🎓 Para tu Presentación

### Demostrar Cobertura ≥ 90%

1. Ejecuta:
   ```bash
   .\mvnw.cmd clean test jacoco:report
   ```

2. Abre el reporte:
   ```bash
   start target\site\jacoco\index.html
   ```

3. Muestra la pantalla principal donde se ve el porcentaje total

4. Navega a cada paquete para mostrar cobertura por componente:
   - `Repositories/`
   - `Services/`
   - `Controllers/`
   - `Entities/`

### Demostrar que las Pruebas Pasan

```bash
.\mvnw.cmd test
```

Muestra la salida de la consola donde se ven todas las pruebas en verde.

### Estadísticas del Proyecto

- **Total de clases de prueba**: 18
- **Total de métodos de prueba**: ~210
- **Cobertura esperada**: ≥ 90%
- **Framework**: JUnit 5 + Mockito + AssertJ
- **Herramienta de cobertura**: JaCoCo

## 💡 Consejos

1. **Ejecuta siempre `clean` antes de `test`** para asegurar resultados frescos
2. **Revisa el reporte de JaCoCo** para identificar áreas sin cobertura
3. **Las pruebas deben pasar siempre** antes de hacer commit
4. **Usa los scripts** (run-tests.bat) para automatizar el proceso

## 🚨 Importante

- ❌ **NO modifiques** el código de producción para hacer pasar las pruebas
- ✅ **SÍ revisa** las líneas sin cobertura para entender si necesitan pruebas
- ✅ **SÍ ejecuta** las pruebas frecuentemente durante el desarrollo
- ✅ **SÍ mantén** la cobertura por encima del 90%

## 📞 Soporte

Si tienes problemas:
1. Revisa la sección "Solución de Problemas" en TEST_README.md
2. Verifica que tienes Java 21 instalado: `java -version`
3. Verifica que Maven funciona: `.\mvnw.cmd -version`

---

**¡Listo para ejecutar y demostrar tu cobertura de pruebas! 🎉**

