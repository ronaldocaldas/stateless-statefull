FROM bellsoft/liberica-openjdk-alpine:17
COPY build/libs/stateful-any-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]