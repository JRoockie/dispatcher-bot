# Telegram-bot - Records-bot

 Приложение состоит из 2х частей: 
1. Telegram бот опросник
2. Web представление данных, собранных ботом

- Telegram bot - @OlegRecords_bot
- Web - records-bot.ru

Стек технологий:

![Java](https://img.shields.io/badge/-java-fcad03?style=for-the-badge&logo=java&logoColor=09000)
![SpringBoot](https://img.shields.io/badge/-springBoot-000000?style=for-the-badge&logo=spring&logoColor=09000)
![JUnit](https://img.shields.io/badge/-junit-ffffff?style=for-the-badge&logo=junit&logoColor=09000)
![Mockito](https://img.shields.io/badge/-mockito-ffffff?style=for-the-badge&logo=mockito&logoColor=09000)
![Docker](https://img.shields.io/badge/-docker-ffffff?style=for-the-badge&logo=docker&logoColor=09000)
![Hibernate](https://img.shields.io/badge/-hibernate-000?style=for-the-badge&logo=hibernate&logoColor=09000)
![Telegram API](https://img.shields.io/badge/-API-ffffff?style=for-the-badge&logo=telegram&logoColor=09000)
![Postgres](https://img.shields.io/badge/-postgres-ffffff?style=for-the-badge&logo=postgresql&logoColor=09000)
![RabbitMQ](https://img.shields.io/badge/-rabbitmq-ffffff?style=for-the-badge&logo=rabbitmq&logoColor=09000)
![GIT](https://img.shields.io/badge/-git-ffffff?style=for-the-badge&logo=git&logoColor=09000)

## Техническое задание

Общие задачи:
- Разработать Телеграм Бот-опросник для сбора информации и составления заявки
- Разработать Веб приложение для просмотра данных
- Разработать фронтенд
- Докеризация проекта
- Деплой и развертывание приложения на удаленном сервере
- Конфгурация NGINX
- Установка домена и SSL

Бекенд бота:
- Бот должен поддерживать казахский и русский языки
- Необходимо легко изменять текст бота в файлах свойств для каждого языка
- Необходимо легко добавлять новые команды и менять структуру бота 
- Автоочистка бд от заявок, которые не были завершены пользователями


Веб приложение:

- Механизм авторизации
- 2 типа заявок: Обработанные и необработанные
- API для фронтенда
- Возможность скачивания файлов в браузер
- CRUD операции
- Реализация JWT
- REST

## О приложении:

Приложение построено c использованием микросервисной архитектуры и состоит из трех приложений:

- browser-admin-view - отправляет данные на фронт.
- logic-core - логика тг бота
- dispatcher - получение сообщений из телеграма и первичная валидация

### Функции dispatcher: 

- [Прием и валидация сообщений из тг](dispatcher/src/main/java/org/voetsky/dispatcherBot/controller/UpdateController.java)
- [Локализация сообщений](dispatcher/src/main/java/org/voetsky/dispatcherBot/configuration/localization/DispatcherLangUnit.java)
- [Отправка сообщения в брокер](dispatcher/src/main/java/org/voetsky/dispatcherBot/service/output/updateProducer/UpdateProducerImpl.java)
- [Прием сообщений из брокера от других сервисов](dispatcher/src/main/java/org/voetsky/dispatcherBot/service/input/answerConsumer/AnswerConsumerImpl.java)
- [Формирование ответа в телеграм ](dispatcher/src/main/java/org/voetsky/dispatcherBot/service/messageutils/MakeMessage.java)

### Функции logic-core: 

- [Получение сообщений из брокера, валидация, первичная обработка](logic-core/src/main/java/org/voetsky/dispatcherBot/services/input)
- [Основная логика обработки комманд и ввода после валидации](logic-core/src/main/java/org/voetsky/dispatcherBot/services/logic/commandHandlerService/CommandHandlerService.java)
- [Сервисы связанные с БД: сравнение обьектов, закачка файлов в БД, общая логика (требующая другие репозитории одновременно)](logic-core/src/main/java/org/voetsky/dispatcherBot/services/repoServices)
- [Заполнение БД тестовыми данными](logic-core/src/main/java/org/voetsky/dispatcherBot/testDataFiller)
- [Локализация сообщений](logic-core/src/main/java/org/voetsky/dispatcherBot/localization)
- [Отправка сообщений в брокер](logic-core/src/main/java/org/voetsky/dispatcherBot/services/output)
- [Автоочистка БД от заявок которые не были до конца завершены пользователем](logic-core/src/main/java/org/voetsky/dispatcherBot/services/scheduleTasks/dbCleanerService/DbCleanerService.java)

### Функции browser-admin-view: 
- [Авторизация](browser-admin-view/src/main/java/org/voetsky/dispatcherBot/controllers/AuthController.java)
- [API для запросов фронта](browser-admin-view/src/main/java/org/voetsky/dispatcherBot/controllers/ViewController.java)
- [Сервис скачивания файлов](browser-admin-view/src/main/java/org/voetsky/dispatcherBot/services/FileOperationsService/FileOperationsService.java)
- [Фильтрация данных из БД](browser-admin-view/src/main/java/org/voetsky/dispatcherBot/services/OrdersService/OrdersOperationsService.java)
- [Регистрация, дефолт логин и пароль для входа](browser-admin-view/src/main/java/org/voetsky/dispatcherBot/services/UserService/UserOperationsService.java)
- [Файлы конфигураций](browser-admin-view/src/main/java/org/voetsky/dispatcherBot/config)

## А как запустить то?
Напиисать в консоли проекта:
```sh
docker compose up -d
```
- Можно изменить порты, токен бота и другое в файлах application.properties каждого микросервиса.
- Не забудьте изменить переменные среды в файле [docker-compose.yaml](docker-compose.yml)

Скачать фронтенд для этого проекта: [frontend](https://github.com/JRoockie/frontend)
Написать в консоли проекта:
```sh
npm i
npm start
```

Открыв http://localhost:3000, увидите все заказы, находящиеся в базе данных.
Напишите в тг @OlegRecords_bot и сделайте заказ. Обновите страницу, новый заказ появится на фронте моментально.

Чтобы зайти на фронт, можно указать дефолтные значения логина и пароля администратора:
- Login: u
- Password: u

## Конфигурация 
Если вы хотите изменить бота, вам нужно написать свой собственный токен и имя бота во всех файлах application.properties и также в файле docker-compose.

```sh
      bot.name: test_bot
      bot.token: 5998422609:AAGqaLslPcF-ZLC0YzzL2efe85UobZ1o0-ZVY
```
