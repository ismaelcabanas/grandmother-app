# API Rest

Este proyecto es un API Rest escrito en Groovy. Es el punto de acceso al BackEnd 
de la aplicación

## Requisitos

Se necesita tener instalado las siguientes herramientas si se 
quiere ejecutar la aplicación

- Java 8
- Maven (La aplicación está verificada con la versión Maven 3.3.9)


## Dockerización de la aplicación

Para ejecutar la aplicación de una forma totalmente aislada, sin dependencias, 
se da la opción de desplegar la aplicación en contenedores Docker, además, esto
me servirá como formación en Docker :).

En este caso, las dependencias que existen en la aplicación son el mecanismo de persistencia (base de datos) y 
la propia aplicación en sí misma. 

### Creando imagen de nuestra aplicación

Para crear una imagen de nuestra aplicación usamos el plugin de Maven 
```xml
<plugin>
	<groupId>com.spotify</groupId>
	<artifactId>docker-maven-plugin</artifactId>
	<version>0.4.11</version>
	<configuration>
		<imageName>${docker.image.prefix}/${project.artifactId}</imageName>
		<dockerDirectory>src/main/docker</dockerDirectory>
		<resources>
			<resource>
				<targetPath>/</targetPath>
				<directory>${project.build.directory}</directory>
				<include>${project.build.finalName}.jar</include>
			</resource>
		</resources>
	</configuration>
</plugin>
```
Y con la instrucción
`mvn docker:build` se genera una imagen Docker de nuestra aplicación Spring Boot.

### Creando la imagen de la base de datos
En este caso, la base de datos a utilizar será una PostgreSQL. Para crearnos una 
imagen de éste basta con ejecutar la instrucción docker `docker pull postgres:9.6.1` y nos 
descarga la imagen Docker de PostgreSQL versión 9.6.1.


## Conectando contenedores

El objetivo es demostrar cómo desplegar la 
aplicación en contenedores Docker. Esto se puede llevar a cabo de dos formas distintas:

### Con contenedores linkados
En esta opción, la solución que se propone es tener un contenedor con la imagen 
de la base de datos de nuestra elección que utilizará la aplicación; y otro 
contenedor que ejecutará la aplicación spring-boot y que estará 
linkado al primer contenedor.

$ docker run -d -p 8080:8080 -e spring.profiles.active=staging-container-postgres --name grandmother-api --link mypostgres ismaelcabanas14/grandmother-api


### Con Docker compose
