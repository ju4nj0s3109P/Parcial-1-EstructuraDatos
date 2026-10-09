# QuindíoExpress

Proyecto académico de Estructura de Datos en Java para la gestión de paquetes de un centro de distribución.

## Estado

Estructura inicial del proyecto. La implementación se desarrollará por etapas, validando cada módulo con pruebas.

## Requisitos previstos

- Java 17+
- Maven
- JUnit 5

## Paquetes principales

- `modelo`: entidades del dominio.
- `estructuras`: colección genérica y lista simplemente enlazada propia.
- `comparadores`: criterios de comparación de paquetes.
- `servicio`: registro, despacho, repartidores, reportes e historial.
- `algoritmos`: recursividad, búsqueda binaria y divide y vencerás.
- `datos`: datos deterministas para demostración y pruebas.

## Nota de diseño

El enunciado presenta dos rangos distintos para la prioridad (0–10 y 1–5). La arquitectura toma provisionalmente el rango explícito de las reglas finales (1–5), con prioridad predeterminada 5, hasta confirmación docente.
