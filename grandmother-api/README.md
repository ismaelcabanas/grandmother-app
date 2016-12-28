# Aplicación de gestión de gastos del piso de mi abuela

## Dockerización de la aplicación

Para ejecutar la aplicación de una forma totalmente aislada, sin dependencias, 
se da la opción de desplegar la aplicación en contenedores Docker.

En este caso, las dependencias son el mecanismo de persistencia (base de datos) y 
la propia aplicación en sí misma. El objetivo es demostrar cómo desplegar la 
aplicación en contenedores Docker.

Esto se puede llevar a cabo de dos formas distintas:

### Con contenedores linkados
En esta opción, la solución que se propone es tener un contenedor con la imagen 
de la base de datos de nuestra elección que utilizará la aplicación; y otro 
contenedor que ejecutará la aplicación spring-boot y que estará 
linkado al primer contenedor que contiene el mecanismo de persistencia.



### Con Docker compose
