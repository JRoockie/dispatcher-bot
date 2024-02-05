# Telegram-bot - Records-bot

 This app consists of 2 parts: 
1. Telegram bot questionnaire
2. Web presentation of data

- Telegram bot - @OlegRecords_bot
- Web view - records-bot.ru

## Functions

- This application completely replaces the manager's work, compiles applications directly from the bot's telegrams and shows them in an easy-to-understand form
- Bot supports Kazakh and Russian languages
- Easy to change bot text in language's property files
- Easy to add new commands
- Web interface

The application is built on a micro service architecture and consists of 3 parts:
- browser-admin-view - sends data to website
- the logic-core - application logic
- dispatcher - receiving messages from telegram

## How to start?
Write in console:
```sh
docker compose up -d
```
> You can change ports, bot token and anything that you need in application.properties files in each microservice. 
> Dont foget to change environment variables in docker-compose.yaml

Download my frontend part: [frontend](https://github.com/JRoockie/fontend)
And write it in frontend part console:
```sh
npm i
npm start
```

Open http://localhost:3000, you can see all orders in database.
Go to the bot @OlegRecords_bot and make order. Then it will appears in our frontend.

You can enter into default admin account:
- Login: u
- Password: u

## Configuration 
If you want to change bot you need to write your own token and botname in all property files and docker-compose also:

```sh
      bot.name: test_bot
      bot.token: 5998422609:AAGqaLslPcF-ZLC0YzzL2efe85UobZ1o0-ZVY
```