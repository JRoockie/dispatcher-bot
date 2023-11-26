package org.voetsky.dispatcherBot.testClasses.commandAnalyzing;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.controller.RepoController;
import org.voetsky.dispatcherBot.services.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.testClasses.testCommands.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
@Component
public class QueueManager {

    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageMakerService messageMakerService;
    private final Queue<TestCommands> testCommands = new ArrayDeque<>();
    private Map<String, Queue<TestCommands>> actions;

    //todo ТУТ ЕГО ЮЫТЬ НЕ ДОЛЖНО, ПЕРЕКИНУТЬ repocontroller в ДРУГОЙ СЕРВИС
    //О нем не должен знать CommandHandler
    //также перекинуть метод INIT И инитить команды в другом месте

    private final RepoController repoController;

    public QueueManager(MessageMakerService messageMakerService, RepoController repoController) {
        this.messageMakerService = messageMakerService;
        this.repoController = repoController;
        init();
    }

    public void init() {

        testCommands.add(new Start());
        testCommands.add(new AskingName());
        testCommands.add(new Fork());
        testCommands.add(new AfterFork());

    }

    public boolean chainCheck(TestCommands testCommands) {
        return testCommands.isChain();
    }


    public HashMap<Boolean, SendMessage> updateReceived(Update update) {

        log.debug("CONTROLLER: Choosing command");
        if (update.hasMessage() && update.getMessage().getText() != null) {
            var key = update.getMessage().getText();
            var chatId = update.getMessage().getChatId().toString();
            if (actions.containsKey(key)) {
                log.debug("CONTROLLER: Executing NON-CALLBACK command part: " + actions.get(key));
                if (chainCheck(actions.get(key).peek())) {
                    var msg = actions.get(key).peek().handle(update);
                } else {
                    var msg = actions.get(key).poll().handle(update);
                }

                bindingBy.put(chatId, key);
                return msg;
            } else if (bindingBy.containsKey(chatId)) {
                var msg = actions.get(bindingBy.get(chatId)).callback(update);
                log.debug("CONTROLLER: Executing CALLBACK command part : "
                        + bindingBy.get(chatId));
                return msg;
            }

        } else if (update.hasCallbackQuery()) {
            log.debug("CONTROLLER: Executing BUTTON ");
            return buttonExecute(update);

        } else if (update.getMessage().hasAudio()) {
            var chatId = update.getMessage().getChatId().toString();
            var msg = actions.get(bindingBy.get(chatId)).callback(update);
            log.debug("CONTROLLER: Executing CALLBACK command part : "
                    + bindingBy.get(chatId));
            return msg;

        } else if (update.getMessage().hasVoice()) {
            var chatId = update.getMessage().getChatId().toString();
            var msg = actions.get(bindingBy.get(chatId)).callback(update);
            log.debug("CONTROLLER: Executing CALLBACK command part : "
                    + bindingBy.get(chatId));
            return msg;
        }
        log.debug("Callback doesn't found, command not found");
        return messageMakerService.makeMap(update, "Command not found, callback not found.");
    }

    public HashMap<Boolean, SendMessage> buttonExecute(Update update) {
        try {
            User user = update.getCallbackQuery().getFrom();
            String callBack = update.getCallbackQuery().getData();
            log.debug("CONTROLLER: Button has pressed by: " + user.getId() + " with attribute: " + callBack);
            if (actions.containsKey(callBack)) {
                HashMap<Boolean, SendMessage> map = actions.get(callBack).handle(update);
                Optional<SendMessage> o = map.values().stream().findFirst();
                SendMessage sendMessage = o.orElseThrow();
                bindingBy.put(update.getCallbackQuery().getFrom().getId().toString(), callBack);
                return messageMakerService.makeMap(sendMessage);
            } else {
                throw new RuntimeException("CONTROLLER: Button command not found");
            }
        } catch (RuntimeException e) {
            return messageMakerService.makeMap(update, "Ошибка нажатия кнопки. Введите /start ");
        }
    }

    public HashMap<Boolean, SendMessage> updateReceivedCommandEvoke(SendMessage s) {
        //todo
        return null;
    }

}
