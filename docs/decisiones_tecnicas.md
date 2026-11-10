# Decisiones técnicas

| Decisión | Motivo | Principio / técnica |
|---|---|---|
| Separar el sistema en `dominio`, `repositorio`, `servicio` y `ui` | Cada paquete tiene una sola razón para cambiar | SRP, alta cohesión |
| Enums `Estado`, `Prioridad` y `TipoSolicitud` | Eliminar `String` mágicos y validar al compilar | Obsesión por primitivos |
| Reglas de transición dentro de `Estado` | Un estado nuevo se agrega en el enum sin tocar el servicio | OCP, Move Method |
| `cambiarEstado`, `asignarResponsable`, `esUrgente` y `resumen` en `Solicitud` | Usan los datos de la solicitud | Move Method, Feature Envy |
| `NuevaSolicitud` y `FiltroSolicitudes` | Agrupar parámetros que viajan juntos | Introduce Parameter Object |
| Interfaz `SolicitudRepository` con implementación CSV y en memoria | Los servicios no dependen del archivo; las pruebas usan memoria | DIP, LSP, ISP |
| Excepciones `ReglaNegocioException` y `PersistenciaException` | Reemplazar `print` + `return` y los `catch` vacíos | Clean Code: manejo de errores |
| Mantener textos, orden de validaciones y formato del CSV | Conservar el comportamiento externo | Refactoring seguro |
| Prueba de regresión contra la salida de la versión inicial | Demostrar que el comportamiento no cambió | Pruebas de regresión |
