# Diagnóstico de calidad – versión inicial (tag `estado-inicial`)

## Calidad externa
Desde la mirada del usuario el sistema funciona: registra solicitudes con validaciones, cambia estados respetando el circuito PENDIENTE → EN_PROCESO → RESUELTA / RECHAZADA, asigna responsables, filtra, lista, genera el reporte y conserva los datos en `solicitudes.csv` entre ejecuciones.

## Calidad interna
Toda la lógica está en `GestorSolicitudes` (311 líneas): valida, aplica reglas de negocio, busca, arma textos, imprime en consola y lee/escribe el archivo. Los estados, prioridades y tipos son `String` repetidos en varios métodos, hay código duplicado, errores silenciados y no existen pruebas automatizadas. Cualquier cambio de regla obliga a tocar varios métodos de la misma clase.

## Code smells y deuda técnica

| # | Smell / debilidad | Ubicación | Evidencia | Impacto y riesgo | Prioridad |
|---|---|---|---|---|---|
| 1 | Clase gigante (God Class) | `GestorSolicitudes` | Valida, aplica reglas, busca, imprime y persiste | Viola SRP; todo cambio pasa por la misma clase | Alta |
| 2 | Método largo | `reporte()` L193-256, `registrar()` L22-68 | 64 y 47 líneas con varias tareas | Difícil de leer y de probar en forma aislada | Alta |
| 3 | Obsesión por primitivos | `Solicitud.estado/prioridad/tipo`; L37, L42, L83, L88-100, L201-224 | Valores de dominio como `String` literales | Un error de tipeo no se detecta al compilar; agregar un valor exige buscarlo en todo el código | Alta |
| 4 | Código duplicado | Búsqueda por id L73-77 y L118-122; formato de línea L169-176 y L183-189 | Mismo bloque copiado | Una corrección debe repetirse en cada copia (viola DRY) | Alta |
| 5 | Errores silenciados | `guardar()` L274, `cargar()` L308 | `catch (Exception ex) { }` vacío | Si falla el archivo se pierden datos sin aviso | Alta |
| 6 | Acoplamiento a consola y archivo | `System.out` en todos los métodos; `FileWriter` en L261 | La lógica depende de detalles de bajo nivel | No se puede probar ni cambiar la persistencia sin modificar la lógica (viola DIP) | Alta |
| 7 | Sin pruebas automatizadas | Todo el proyecto | No hay carpeta de tests | No hay red de seguridad para cambiar el código | Alta |
| 8 | Demasiados parámetros | `registrar(leg, nom, mail, t, p, desc)` L22 | 6 `String` seguidos | Fácil invertir argumentos sin que el compilador lo note | Media |
| 9 | Cadena de if-else | Transiciones de estado L88-100 | Un `if` por estado | Agregar un estado obliga a modificar el método (viola OCP) | Media |
| 10 | Envidia de funcionalidad (Feature Envy) | Formato L169-176, urgencia L229 | El gestor usa los datos de `Solicitud` para decidir por ella | Reglas de la solicitud dispersas fuera de su clase | Media |
| 11 | Clases de datos con campos públicos | `Solicitud`, `Estudiante`, `Responsable` | Atributos `public` sin validación | Cualquier clase puede dejar un objeto en estado inválido | Media |
| 12 | Nombres poco descriptivos | `l`, `c`, `t`, `p`, `e`, `x`, `pen`, `a`, `m`, `b` | Variables de una o dos letras | Hay que leer todo el método para entender cada variable | Media |
| 13 | Valores mágicos | `10` (L49), `"\\d{5,6}"` (L27), `";"`, `"solicitudes.csv"` | Literales sin nombre | Reglas de negocio ocultas en el código | Baja |
| 14 | Comentarios redundantes | `// cambia el estado`, `// lista todas` | Repiten el nombre del método | No aportan información y se desactualizan | Baja |
