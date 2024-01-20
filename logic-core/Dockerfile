FROM bellsoft/liberica-openjdk-debian:18 as build
#FROM --platform=linux/amd64 bellsoft/liberica-openjdk-debian:16 as build
ENV LANG=ru_RU.UTF-8 LANGUAGE=ru_RU:ru
COPY src src
COPY pom.xml pom.xml
WORKDIR /app
COPY target/logic-core.jar logic-core.jar
ENTRYPOINT ["java", "-jar", "logic-core.jar"]