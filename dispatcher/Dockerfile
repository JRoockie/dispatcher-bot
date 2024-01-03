FROM bellsoft/liberica-openjdk-debian:16 as build
ENV LANG=ru_RU.UTF-8 LANGUAGE=ru_RU:ru

#FROM maven:3.9.5
COPY src src
COPY pom.xml pom.xml
#RUN mvn clean package
#RUN adduser --system spring-boot && RUN addgroup --system spring-boot && RUN adduser spring-boot spring-boot
#USER spring-boot
WORKDIR /app
#COPY --from=build target/dispatcher.jar dispatcher.jar
#ADD target/dispatcher.jar dispatcher.jar
COPY target/dispatcher.jar dispatcher.jar
#ARG JAR_FILE=target/dispatcher.jar
#COPY build/lib/*.jar app.jar
#COPY ${JAR_FILE} dispatcher.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "./dispatcher.jar"]