FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/grandmother-api-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]