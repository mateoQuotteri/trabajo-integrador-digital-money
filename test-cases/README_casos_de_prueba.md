# Planilla de Casos de Prueba — Digital Money House

## Archivo principal
`casos_de_prueba_sprint1_sprint2.csv`

---

## Resumen de casos por sprint y suite

| Sprint | Suite | Cantidad | IDs |
|--------|-------|----------|-----|
| Sprint 1 | Smoke | 6 | TC-S1-001, 005, 008, 010, 012, 013 |
| Sprint 1 | Regression | 7 | TC-S1-002, 003, 004, 006, 007, 009, 011 |
| Sprint 2 | Smoke | 9 | TC-S2-001, 005, 007, 010, 011, 015, 017, 019 |
| Sprint 2 | Regression | 14 | TC-S2-002, 003, 004, 006, 008, 009, 012, 013, 014, 016, 018, 020, 021, 022, 023 |
| **Total** | | **36** | |

---

## Funcionalidades cubiertas

### Sprint 1 (mantenimiento)
- Registro de usuario (`POST /users/register`)
- Login (`POST /users/login`)
- Logout (`POST /users/logout`)
- Ver perfil (`GET /users/{id}`)
- Ver cuenta (`GET /accounts/{id}`)
- Crear cuenta (`POST /accounts`)

### Sprint 2 (nuevas funcionalidades)
- Editar perfil de usuario (`PATCH /users/{id}`)
- Editar alias de cuenta (`PATCH /accounts/{id}`)
- Ver historial de transacciones (`GET /accounts/{id}/transactions`)
- Agregar tarjeta (`POST /accounts/{id}/cards`)
- Listar tarjetas (`GET /accounts/{id}/cards`)
- Ver tarjeta específica (`GET /accounts/{id}/cards/{cardId}`)
- Eliminar tarjeta (`DELETE /accounts/{id}/cards/{cardId}`)
- Seguridad: acceso sin token / token expirado

---

## Suite Smoke — Casos a ejecutar primero

Estos casos validan que las funcionalidades críticas estén operativas.

| ID | Descripción |
|----|-------------|
| TC-S1-001 | Registro exitoso de usuario nuevo |
| TC-S1-005 | Login exitoso con credenciales válidas |
| TC-S1-008 | Logout exitoso con token válido |
| TC-S1-010 | Ver perfil propio autenticado |
| TC-S1-012 | Ver cuenta propia |
| TC-S1-013 | Crear cuenta para usuario autenticado |
| TC-S2-001 | Editar nombre del usuario autenticado |
| TC-S2-005 | Editar alias de cuenta propia |
| TC-S2-007 | Ver historial de transacciones de cuenta propia |
| TC-S2-010 | Agregar tarjeta de débito exitosamente |
| TC-S2-011 | Agregar tarjeta de crédito exitosamente |
| TC-S2-015 | Listar todas las tarjetas de la cuenta propia |
| TC-S2-017 | Ver tarjeta específica por ID |
| TC-S2-019 | Eliminar tarjeta existente exitosamente |

---

## Suite Regression — Casos de borde y errores

Se ejecutan después del Smoke para validar casos de error, validaciones y seguridad.

| ID | Descripción |
|----|-------------|
| TC-S1-002 | Registro con email duplicado |
| TC-S1-003 | Registro con campos requeridos vacíos |
| TC-S1-004 | Registro con DNI inválido |
| TC-S1-006 | Login con contraseña incorrecta |
| TC-S1-007 | Login con usuario inexistente |
| TC-S1-009 | Logout sin token |
| TC-S1-011 | Intentar ver perfil de otro usuario |
| TC-S2-002 | Editar contraseña del usuario |
| TC-S2-003 | Editar teléfono del usuario |
| TC-S2-004 | Intentar editar perfil de otro usuario |
| TC-S2-006 | Intentar editar alias de cuenta ajena |
| TC-S2-008 | Ver transacciones cuando no hay historial |
| TC-S2-009 | Intentar ver transacciones de cuenta ajena |
| TC-S2-012 | Agregar tarjeta con número inválido |
| TC-S2-013 | Agregar tarjeta con campos vacíos |
| TC-S2-014 | Agregar tarjeta duplicada |
| TC-S2-016 | Listar tarjetas cuando la cuenta no tiene tarjetas |
| TC-S2-018 | Ver tarjeta con ID inexistente |
| TC-S2-020 | Eliminar tarjeta con ID inexistente |
| TC-S2-021 | Eliminar tarjeta de otra cuenta |
| TC-S2-022 | Acceder sin token JWT |
| TC-S2-023 | Acceder con token expirado |

---

## Columnas del archivo CSV

| Columna | Descripción |
|---------|-------------|
| ID | Identificador único del caso |
| Sprint | Sprint al que pertenece |
| Suite | Smoke o Regression |
| Módulo | Área funcional |
| Nombre del caso | Descripción breve |
| Precondiciones | Estado del sistema previo a la ejecución |
| Pasos | Secuencia de pasos separados por `|` |
| Datos de entrada | Request body / headers / path params |
| Resultado esperado | Comportamiento correcto esperado |
| Resultado obtenido | Completar durante la ejecución |
| Estado | PASS / FAIL / BLOQUEADO / NO EJECUTADO |
| Prioridad | Alta / Media / Baja |
| Observaciones | Notas adicionales |

---

## Herramienta recomendada para ejecución
Postman o cualquier cliente HTTP REST. Todos los endpoints se acceden a través del Gateway en `http://localhost:8080`.
