FROM eclipse-temurin:17.0.8_7-jre as build
ARG JAR_FILE=target/dispatcher-1.0-SNAPSHOT.jar
EXPOSE 8084

#COPY build/lib/*.jar app.jar
COPY ${JAR_FILE} dispatcher.jar
ENTRYPOINT ["java", "-jar", "dispatcher.jar"]