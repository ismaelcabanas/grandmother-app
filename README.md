Travis CI:
[![Build Status](https://travis-ci.org/ismaelcabanas/grandmother-app.svg?branch=development)](https://travis-ci.org/ismaelcabanas/grandmother-app)

Coverity Scan:
[![Coverity Scan Build Status](https://scan.coverity.com/projects/11594/badge.svg)](https://scan.coverity.com/projects/11594)


# Aplicación de gestión de gastos del piso de mi abuela

Esta aplicación surge por la necesidad de gestionar los depósitos y gastos que se realizan los hijos de mi abuela sobre la cuenta 
compartida del piso.

Mi idea original es hacer un API Rest con el que pueda realizar estas gestiones, y posteriormente agregar soportes como 
web y mobile. Esta aplicación también me servirá como formación para aplicar distintas tecnologías de desarrollo, sobre 
todo como pruebas de concepto de las últimas tecnologías que van surgiendo, también, lo que el tiempo me permita realizar.

# Módulos de la aplicación

- **grandmother-api**: módulo API Rest desarrollado con Spring Boot 1.4
- **grandmother-core**: módulo Core que contiene la lógica de negocio de la aplicación

# Requisitos

Se necesitará instalar las siguientes herramientas para poder construir y ejecutar la aplicación API Rest:

- [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](http://maven.apache.org/)(La aplicación está probada con Maven 3.3.9)
- Git

# Ejecutando los Tests

Se puede ejecutar los tests unitarios ejecutando el comando:

    mvn clean test -P develop

Se puede ejecutar los tests de integración ejecutando el comando:

    mvn clean verify -P integration-test

# Ejecutando la aplicación

La aplicación ejecutable se encuentra en el módulo **grandmother-api**. Previamente 
se necesita tener instaladas en el repositorio Maven las dependencias del módulo **grandmother-core**, 
de forma que debemos ejecutar previamente el comando:
    
    mvn clean install
    
Se puede ejecutar la aplicación localmente ejecutando:

    mvn spring-boot:run


