FROM openjdk:8-jdk-alpine
VOLUME /tmp

COPY target/demo-0.0.1-SNAPSHOT.jar /app/lib/ws.jar

ENTRYPOINT ["java","-cp", "/app/lib/*.jar", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/lib/ws.jar"]
