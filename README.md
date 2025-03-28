# Proyecto de curso: Icesi Trade

## Integrantes:
- Sara Lucia Diaz Puerta
- Sebastian Erazo Ochoa
- Oscar Muñoz Ramirez

## Propuesta de modelo.
[Modelo relacional](docs/MR-IcesiTrade.pdf)

## Compilar y Ejecutar el Proyecto

Para compilar y ejecutar la aplicación, usa el siguiente comando:
```sh
mvn clean install
mvn spring-boot:run
```

## Ejecución de Pruebas con Reporte JaCoCo

Para ejecutar las pruebas y generar el reporte de cobertura con JaCoCo, utiliza:
```sh
mvn clean test
```

Para generar el reporte de cobertura en HTML:
```sh
mvn jacoco:report
```

El informe se generará en:
```
target/site/jacoco/index.html
```

Puedes abrirlo en un navegador para revisar la cobertura de código.

