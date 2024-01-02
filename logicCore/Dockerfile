FROM bellsoft/liberica-openjdk-debian:16 as build

COPY src src
COPY pom.xml pom.xml
WORKDIR /app
COPY target/logicCore.jar logicCore.jar
ENTRYPOINT ["java", "-jar", "logicCore.jar"]