# Gestión de Solicitudes Académicas

Trabajo Práctico Integrador de Metodología de Sistemas II (UTN – TUP). Sistema de consola en Java que permite registrar solicitudes de estudiantes, cambiar su estado, asignar un responsable, buscarlas y obtener un reporte. El objetivo del trabajo es mejorar la calidad interna de una versión inicial sin alterar su comportamiento externo.

## Funcionalidades

- Registrar una solicitud (legajo, nombre, email, tipo, prioridad y descripción).
- Cambiar el estado: PENDIENTE → EN_PROCESO → RESUELTA, o RECHAZADA desde PENDIENTE o EN_PROCESO.
- Asignar un responsable y su área (obligatorio para pasar a EN_PROCESO).
- Buscar por tipo, estado y prioridad, y listar todas las solicitudes.
- Reporte por estado, prioridad y tipo, con solicitudes urgentes y sin responsable.
- Persistencia en el archivo `solicitudes.csv`.

## Tecnologías

Java 17, Maven y JUnit 4.

## Instalación y uso

Con Maven:

```
mvn test
mvn compile exec:java
```

Sin Maven:

```
javac -encoding UTF-8 -d out src/main/java/ar/edu/utn/solicitudes/*.java src/main/java/ar/edu/utn/solicitudes/*/*.java
java -cp out ar.edu.utn.solicitudes.Main
```

## Estructura

```
src/main/java/ar/edu/utn/solicitudes/
  dominio/      Solicitud, Estudiante, Responsable, enums y reglas de negocio
  repositorio/  SolicitudRepository y sus implementaciones CSV y en memoria
  servicio/     SolicitudService y ReporteService
  ui/           ConsolaApp
src/test/java/  Pruebas unitarias y prueba de regresión
pruebas/        Escenario de regresión y salida de referencia de la versión inicial
docs/           Diagnóstico, decisiones técnicas, checklist de code review y evidencias
```

## Tags

| Tag | Contenido |
|---|---|
| `estado-inicial` | Versión inicial con los problemas de diseño |
| `etapa-diagnostico` | Diagnóstico y escenario de regresión con la salida de referencia |
| `etapa-refactoring` | Refactorings aplicados |
| `etapa-pruebas` | Pruebas unitarias y de regresión |
| `etapa-docs` | README, decisiones técnicas y checklist de code review |
| `estado-refactorizado` | Versión final con las correcciones del code review |

## Autor

Martín Rotolo – Comisión M25 C4-10.
