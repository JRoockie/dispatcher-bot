FROM bellsoft/liberica-openjdk-debian:16 as build

FROM maven:3.9.5
COPY src src
COPY pom.xml pom.xml

RUN mvn clean package

WORKDIR /view

ARG JAR_FILE=target/browserAdminView.jar

COPY ${JAR_FILE} /view/browserAdminView.jar

RUN ls -l ./
RUN ls -l /view
ENTRYPOINT ["java", "-jar", "view/browserAdminView.jar"]

#
#FROM maven:3.9.4-openjdk-17 as builder
#WORKDIR /app
#COPY . /app/.
#RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true
#
#FROM bellsoft/liberica-openjdk-debian:16
#WORKDIR /app
#COPY --from=builder /app/target/*.jar /app/*.jar
#EXPOSE 8181
#ENTRYPOINT ["java", "-jar", "/app/*.jar"]

## Выбираем Maven образ для сборки
#FROM maven:3.8.4-openjdk-17 as builder
#
#RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true
#
#FROM bellsoft/liberica-openjdk-debian:16
#
#WORKDIR /app
#
## Копируем JAR-файл из предыдущего этапа сборки
#COPY --from=builder /app/target/*.jar /app/app.jar
#
## Открываем порт 8084 (или нужный вам порт)
#EXPOSE 8084
#
## Запускаем приложение
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]
