# Code review – versión refactorizada (tag `etapa-docs`)

Revisión aplicada sobre el código del tag `etapa-docs`, antes del cierre en `estado-refactorizado`.

## Checklist técnico

| Categoría | Pregunta | Resultado |
|---|---|---|
| Lógica y funcionalidad | ¿Hace lo mismo que la versión inicial, incluidos los casos de error? | Sí. La prueba de regresión compara salida y CSV con la versión inicial |
| Lógica y funcionalidad | ¿Cubre casos de borde (id inexistente, estado inválido, solicitud cerrada)? | Sí, en el escenario de regresión y en las pruebas unitarias |
| Legibilidad | ¿Los nombres son descriptivos y del dominio? | Sí |
| Legibilidad | ¿Los métodos son cortos y hacen una sola cosa? | Sí. Observación CR-2 sobre el formato del `switch` |
| Diseño | ¿Cada clase tiene una sola responsabilidad (SRP)? | Sí: dominio, repositorio, servicio y consola separados |
| Diseño | ¿Los servicios dependen de abstracciones (DIP)? | Sí, de `SolicitudRepository` |
| Diseño | ¿Hay código duplicado (DRY)? | No se encontró |
| Manejo de errores | ¿Se usan excepciones y no hay `catch` vacíos? | Sí. Observación CR-1 sobre la captura de `NullPointerException` |
| Seguridad | ¿Se usan datos personales reales? | No, todos los datos son ficticios |
| Seguridad | ¿Las entradas se validan? | Sí. Observación CR-4 sobre el separador del CSV |
| Rendimiento | ¿Hay recorridos o escrituras innecesarias? | Observación CR-5 sobre la escritura del archivo |
| Pruebas | ¿Cada componente tiene pruebas? | Observación CR-3: falta probar el repositorio CSV |

## Observaciones y feedback

| ID | Observación | Feedback | Acción | Estado |
|---|---|---|---|---|
| CR-1 | `Valores.convertir` captura `NullPointerException` | Capturar NPE oculta errores de programación. Se sugiere validar el `null` en forma explícita | Se agregó la validación y se quitó la captura | Corregido |
| CR-2 | En `ConsolaApp.procesarOpcion` cada `case` está en una sola línea con su `break` | Rompe el estilo del resto del proyecto y dificulta la lectura | Se reformateó un `case` por bloque | Corregido |
| CR-3 | `CsvSolicitudRepository` no tiene pruebas propias | La carga del archivo solo se prueba indirectamente | Se agregó `CsvSolicitudRepositoryTest` | Corregido |
| CR-4 | Si una descripción contiene `;` el registro del CSV queda corrupto | El problema ya existía en la versión inicial; corregirlo cambia el comportamiento externo | Se registra como mejora futura | Pendiente |
| CR-5 | Cada guardado reescribe el archivo completo | Aceptable para el volumen de una sede; no escala a miles de solicitudes | Se registra como mejora futura | Pendiente |

Aspectos positivos: la separación por paquetes es clara, las reglas de negocio quedaron en el dominio y la prueba de regresión da una red de seguridad para futuros cambios.

## Criterios de aceptación técnica

| Criterio | Cumple |
|---|---|
| El código compila sin errores | Sí |
| Todas las pruebas pasan (`mvn test`) | Sí, 22 pruebas |
| La salida y el CSV coinciden con la versión inicial | Sí |
| Se respetan SRP y DIP | Sí |
| No hay `catch` vacíos ni errores silenciados | Sí |
| No hay datos personales reales | Sí |
| Las observaciones de prioridad alta están resueltas | Sí (CR-1 a CR-3) |
