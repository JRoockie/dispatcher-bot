FROM bellsoft/liberica-openjdk-debian:18 as build
COPY src src
COPY pom.xml pom.xml
WORKDIR /app
COPY target/dispatcher.jar dispatcher.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "./dispatcher.jar"]