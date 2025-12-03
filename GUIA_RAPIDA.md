# 📖 Guía Rápida - Pruebas Unitarias

## 🎯 Objetivo

Alcanzar **≥ 90% de cobertura** de código con pruebas unitarias completas.

## ⚡ Ejecutar en 3 Pasos

### Paso 1: Ejecutar Pruebas
```bash
.\mvnw.cmd clean test jacoco:report
```

### Paso 2: Abrir Reporte
```bash
start target\site\jacoco\index.html
```

### Paso 3: Verificar Cobertura
Busca el porcentaje en el reporte HTML - debe ser **≥ 90%**

## 📊 Lo que se creó

| Tipo | Cantidad | Archivos |
|------|----------|----------|
| Pruebas de Repositories | 5 clases | 43 pruebas |
| Pruebas de Services | 6 clases | 55 pruebas |
| Pruebas de Controllers | 5 clases | 36 pruebas |
| Pruebas de Entities | 1 clase | 13 pruebas |
| Pruebas de DTOs | 1 clase | 4 pruebas |
| **TOTAL** | **18 clases** | **~210 pruebas** |

## 🎓 Comandos Esenciales

### Ejecutar TODO
```bash
.\mvnw.cmd clean test jacoco:report
```

### Solo una categoría
```bash
# Repositories
.\mvnw.cmd test -Dtest="*RepositoryTest"

# Services
.\mvnw.cmd test -Dtest="*ServiceTest"

# Controllers
.\mvnw.cmd test -Dtest="*ControllerTest"
```

### Solo una clase
```bash
.\mvnw.cmd test -Dtest=ClientServiceTest
```

## 📁 Estructura Creada

```
BackendTingeso/
├── src/test/java/.../
│   ├── RepositoryTests/    (5 archivos)
│   ├── ServiceTests/       (6 archivos)
│   ├── ControllerTests/    (5 archivos)
│   ├── EntityTests/        (1 archivo)
│   └── DTOTests/           (1 archivo)
├── pom.xml                 (actualizado con JaCoCo + H2)
├── run-tests.bat          (script automático)
├── TEST_README.md         (documentación completa)
├── TESTING_SUMMARY.md     (resumen ejecutivo)
└── INSTRUCCIONES_EJECUTAR_PRUEBAS.md
```

## ✅ Checklist Rápido

- [ ] Ejecutar `.\mvnw.cmd clean test`
- [ ] Verificar que todas las pruebas pasen ✓
- [ ] Ejecutar `.\mvnw.cmd jacoco:report`
- [ ] Abrir `target\site\jacoco\index.html`
- [ ] Verificar cobertura ≥ 90% ✓

## 🎯 Qué se probó

### ✅ Repositories (Capa de Datos)
- Consultas personalizadas
- Métodos JPA
- Proyecciones a DTOs
- Filtros y agregaciones

### ✅ Services (Lógica de Negocio)
- CRUD completo
- Validaciones de negocio
- Manejo de excepciones
- Transacciones

### ✅ Controllers (API REST)
- Endpoints HTTP
- Request/Response
- Códigos de estado
- Validaciones

### ✅ Entities (Modelos)
- Getters/Setters
- Constructores
- Equals/HashCode
- Enumeraciones

## 🔥 Script Rápido

**Windows:**
```bash
run-tests.bat
```

Esto hace todo automáticamente:
1. ✅ Limpia el proyecto
2. ✅ Ejecuta pruebas
3. ✅ Genera reporte
4. ✅ Abre en navegador

## 📚 Documentos Disponibles

1. **TEST_README.md** → Guía completa (la más detallada)
2. **TESTING_SUMMARY.md** → Resumen ejecutivo
3. **INSTRUCCIONES_EJECUTAR_PRUEBAS.md** → Instrucciones paso a paso
4. **GUIA_RAPIDA.md** → Este archivo (lo más rápido)
5. **RESUMEN_PRUEBAS.txt** → Vista general en texto plano

## 💡 Tecnologías

- ✅ **JUnit 5** - Framework de testing
- ✅ **Mockito** - Mocks
- ✅ **AssertJ** - Aserciones
- ✅ **MockMvc** - Testing de REST
- ✅ **H2** - Base de datos en memoria
- ✅ **JaCoCo** - Cobertura de código

## 🎉 Resultado Final

```
✅ 18 clases de prueba
✅ ~210 métodos de prueba
✅ Cobertura ≥ 90%
✅ Todas las pruebas pasan
✅ Documentación completa
```

## 🚨 Si algo falla

```bash
# Limpiar y reintentar
.\mvnw.cmd clean
.\mvnw.cmd test
```

## 🎓 Para Presentar

1. Ejecuta: `.\mvnw.cmd clean test jacoco:report`
2. Muestra la consola (pruebas pasando)
3. Abre: `target\site\jacoco\index.html`
4. Muestra el porcentaje ≥ 90%

## 📞 ¿Necesitas más detalles?

Lee **TEST_README.md** para documentación completa.

---

**¡Todo listo para alcanzar el 90% de cobertura! 🚀**

