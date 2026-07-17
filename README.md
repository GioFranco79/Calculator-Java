# Calculadora Online — Backend REST API

Backend de una calculadora en línea construido con **Java 21** y **Spring Boot 3.5.4**. Expone una API REST con las operaciones aritméticas básicas más raíz cuadrada y potencia, con manejo centralizado de errores de dominio y una suite de pruebas unitarias con cobertura del 100 %.

---

## Tecnologías

| Herramienta | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.4 |
| Maven | 3.x |
| JUnit 5 | (incluido en Spring Boot) |
| Mockito | (incluido en Spring Boot) |
| AssertJ | (incluido en Spring Boot) |
| JaCoCo | 0.8.12 |

---

## Estructura del proyecto

```
src/
├── main/java/com/calculator/
│   ├── CalculatorApiApplication.java   ← Punto de entrada
│   ├── controller/
│   │   └── CalculatorController.java   ← Endpoints REST
│   ├── service/
│   │   └── CalculatorService.java      ← Lógica de negocio
│   ├── exception/
│   │   ├── CalculatorException.java        ← Excepción base
│   │   ├── DivisionByZeroException.java    ← División por cero
│   │   ├── NegativeSqrtException.java      ← Raíz de número negativo
│   │   └── GlobalExceptionHandler.java     ← Manejador centralizado (@RestControllerAdvice)
│   └── dto/
│       ├── OperationRequest.java       ← Cuerpo de petición binaria
│       ├── UnaryOperationRequest.java  ← Cuerpo de petición unaria
│       ├── OperationResponse.java      ← Respuesta exitosa
│       └── ErrorResponse.java          ← Respuesta de error
└── test/java/com/calculator/
    ├── service/
    │   └── CalculatorServiceTest.java          ← 36 tests de lógica pura
    ├── exception/
    │   ├── CalculatorExceptionTest.java        ← 8 tests de jerarquía de excepciones
    │   └── GlobalExceptionHandlerTest.java     ← 10 tests del manejador de errores
    ├── controller/
    │   └── CalculatorControllerTest.java       ← 20 tests de capa web
    └── listener/
        └── DisplayNameListener.java            ← Listener que imprime @DisplayName en consola
```

---

## Requisitos previos

- **Java 21** o superior instalado y configurado en `JAVA_HOME`
- **Maven 3.6+** instalado

Verificar versiones:

```bash
java -version
mvn -version
```

---

## Cómo ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd Calculadora
```

### 2. Compilar

```bash
mvn compile
```

### 3. Levantar el servidor

```bash
mvn spring-boot:run
```

El servidor queda disponible en `http://localhost:8080`.

### 4. Generar el JAR ejecutable

```bash
mvn package
java -jar target/calculator-api-1.0.0.jar
```

---

## Endpoints de la API

Todos los endpoints reciben y devuelven **JSON**. La URL base es `/api/calculator`.

### Operaciones binarias — `POST`

| Endpoint | Operación | Body |
|---|---|---|
| `/api/calculator/add` | Suma | `{ "operandA": 5, "operandB": 3 }` |
| `/api/calculator/subtract` | Resta | `{ "operandA": 5, "operandB": 3 }` |
| `/api/calculator/multiply` | Multiplicación | `{ "operandA": 5, "operandB": 3 }` |
| `/api/calculator/divide` | División | `{ "operandA": 5, "operandB": 3 }` |
| `/api/calculator/power` | Potencia | `{ "operandA": 2, "operandB": 10 }` |

### Operaciones unarias — `POST`

| Endpoint | Operación | Body |
|---|---|---|
| `/api/calculator/sqrt` | Raíz cuadrada | `{ "operand": 25 }` |

### Ejemplo de respuesta exitosa — `200 OK`

```json
{
  "operation": "add",
  "result": 8.0
}
```

### Ejemplo de error de dominio — `422 Unprocessable Entity`

```json
{
  "errorCode": "DIVISION_BY_ZERO",
  "message": "Division por cero no esta permitida.",
  "timestamp": "2026-07-17T16:00:00.000"
}
```

### Ejemplo de error de validación — `400 Bad Request`

```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "operandB: operandB must not be null",
  "timestamp": "2026-07-17T16:00:00.000"
}
```

---

## Manejo de errores

El backend tiene excepciones de dominio propias para proteger las operaciones matemáticas:

| Situación | Excepción | HTTP |
|---|---|---|
| Divisor igual a cero | `DivisionByZeroException` | `422` |
| Raíz cuadrada de número negativo | `NegativeSqrtException` | `422` |
| Campo nulo en el cuerpo de la petición | Validación `@NotNull` | `400` |
| Error inesperado del servidor | `Exception` genérica | `500` |

---

## Pruebas unitarias

### Ejecutar los tests

```bash
mvn test
```

Al correr este comando se imprime en consola el nombre en español de cada test (definido con `@DisplayName`), agrupado por clase y método:

```
══════════════════════════════════════════════════════════════
  EJECUCIÓN DE PRUEBAS UNITARIAS
══════════════════════════════════════════════════════════════
  [TEST] ✔  CalculatorService – Unit Tests > add() > suma dos positivos
  [TEST] ✔  CalculatorService – Unit Tests > add() > suma dos negativos
  [TEST] ✔  CalculatorService – Unit Tests > divide() > divide dos positivos
  [TEST] ✔  CalculatorService – Unit Tests > divide() > BRANCH: divisor = 0 → lanza DivisionByZeroException
  ...
══════════════════════════════════════════════════════════════
```

### Descripción de la suite

La suite está organizada en 4 clases de test con un total de **74 tests**.

#### `CalculatorServiceTest` — Lógica de negocio (36 tests)

Prueba directamente el servicio sin Spring. Cada operación tiene su propio grupo `@Nested`:

| Grupo | Casos cubiertos |
|---|---|
| `add()` | Positivos, negativos, mixtos, cero, decimales |
| `subtract()` | Resultado positivo, negativo, cero, mismo número |
| `multiply()` | Por cero, por uno, negativos, decimales |
| `divide()` | Cociente entero, decimal, numerador cero, **divisor = 0 → excepción**, **divisor = -0.0 → excepción** |
| `sqrt()` | Cuadrado perfecto, cero, irracional, decimal, **negativo → excepción**, **-∞ → excepción** |
| `power()` | Exponente positivo, cero, uno, negativo, fraccionario, base negativa par/impar |

#### `CalculatorExceptionTest` — Jerarquía de excepciones (8 tests)

Verifica que cada clase de excepción asigne correctamente su `errorCode`, `message` y relación de herencia con `CalculatorException` y `RuntimeException`.

#### `GlobalExceptionHandlerTest` — Manejador de errores (10 tests)

Prueba cada `@ExceptionHandler` de forma aislada usando Mockito para simular `MethodArgumentNotValidException`. Verifica el código HTTP, el `errorCode` y el `message` de cada respuesta.

| Handler | HTTP esperado |
|---|---|
| `DivisionByZeroException` | `422` |
| `NegativeSqrtException` | `422` |
| `CalculatorException` genérica | `400` |
| `MethodArgumentNotValidException` | `400` |
| `ConstraintViolationException` | `400` |
| `Exception` genérica | `500` |

#### `CalculatorControllerTest` — Capa web (20 tests)

Usa `@WebMvcTest` y `@MockitoBean` para aislar completamente el controlador del servicio. Verifica código HTTP, estructura JSON de la respuesta y que el controlador delega correctamente al servicio.

Cada endpoint tiene tres ramas cubiertas:
- Petición válida → `200 OK`
- Campo nulo → `400 Bad Request`
- Excepción de dominio propagada → `422 Unprocessable Entity`

### Cobertura de código

Para generar el reporte completo de cobertura (line coverage + branch coverage):

```bash
mvn verify
```

El reporte HTML se genera en:

```
target/site/jacoco/index.html
```

El build **falla automáticamente** si la cobertura de cualquier paquete de negocio cae por debajo del **100 %** en líneas o ramas. Los DTOs y la clase de arranque están excluidos de esta verificación.

---

## Prueba rápida con curl

```bash
# Suma
curl -X POST http://localhost:8080/api/calculator/add \
  -H "Content-Type: application/json" \
  -d '{"operandA": 10, "operandB": 5}'

# División por cero (debe devolver 422)
curl -X POST http://localhost:8080/api/calculator/divide \
  -H "Content-Type: application/json" \
  -d '{"operandA": 8, "operandB": 0}'

# Raíz cuadrada
curl -X POST http://localhost:8080/api/calculator/sqrt \
  -H "Content-Type: application/json" \
  -d '{"operand": 144}'

# Potencia
curl -X POST http://localhost:8080/api/calculator/power \
  -H "Content-Type: application/json" \
  -d '{"operandA": 2, "operandB": 8}'
```
