#FROM bellsoft/liberica-openjdk-debian:16 as build
#
#FROM maven:3.9.5
#COPY src src
#COPY pom.xml pom.xml
#
#RUN mvn clean package
#
#WORKDIR /view
#
#ARG JAR_FILE=target/browserAdminView.jar
#
#COPY ${JAR_FILE} /view/browserAdminView.jar
#
#RUN ls -l ./
#RUN ls -l /view
#ENTRYPOINT ["java", "-jar", "view/browserAdminView.jar"]

FROM bellsoft/liberica-openjdk-debian:16 as build
ENV LANG=ru_RU.UTF-8 LANGUAGE=ru_RU:ru
EXPOSE 8086
COPY src src
COPY pom.xml pom.xml
WORKDIR /app
COPY target/browser-admin-view.jar browser-admin-view.jar
ENTRYPOINT ["java", "-jar", "./browser-admin-view.jar"]