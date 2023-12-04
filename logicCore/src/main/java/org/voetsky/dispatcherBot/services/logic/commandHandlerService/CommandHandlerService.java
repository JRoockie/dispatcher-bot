package org.voetsky.dispatcherBot.services.logic.commandHandlerService;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.services.logic.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.logic.logicUtil.CommandInit;
import org.voetsky.dispatcherBot.services.logic.logicUtil.Initialization;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
@Getter
@Component
public class CommandHandlerService implements CommandHandler {

    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageMakerService messageMakerService;
    private Map<String, CommandInterface> actions;
    private final Initialization initialization;

    public CommandHandlerService(MessageMakerService messageMakerService, RepoController repoController, CommandInit commandInit) {
        this.messageMakerService = messageMakerService;
        this.initialization = commandInit;
    }

    @PostConstruct
    public void initCommands(){
        actions = initialization.initCommands();
    }

    public SendMessage updateReceived(Update update) {
        var text = getMessageText(update);
        var chatId = getChatId(update);

        if (update.hasMessage() && update.getMessage().getText() != null) {
            return processCommand(update, text, chatId);
        } else if (update.hasCallbackQuery()) {
            return processButton(update, text, chatId);
        } else if (update.getMessage().hasAudio()) {
            return processCallBack(update, chatId);
        } else if (update.getMessage().hasVoice()) {
            return processCallBack(update, chatId);
        }
        throw new LogicCoreException("Непредвиденная ошибка");
    }

    public SendMessage processCommand(Update update, String text, String chatId) {
        if (actions.containsKey(text)) {
            return processHandle(update, text, chatId);
        } else if (bindingBy.containsKey(chatId)) {
            return processCallBack(update, chatId);
        }
        throw new LogicCoreException("Ошибка команды");
    }

    public SendMessage processHandle(Update update, String text, String chatId) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("NON-CALLBACK command part: %s",
                    actions.get(text)));
        }
        var msg = actions.get(text).handle(update);
        bindingBy.put(chatId, text);
        return processChain(msg, update);
    }

    public SendMessage processCallBack(Update update, String chatId) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Executing CALLBACK command part: %s",
                    bindingBy.get(chatId)));
        }
        var msg = actions.get(bindingBy.get(chatId)).callback(update);
        return processChain(msg, update);
    }

    @Override
    public SendMessage processButton(Update update, String text, String chatId) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Button has pressed by: %s with attribute: %s",
                    chatId, text));
        }
        return processHandle(update, text, chatId);
    }

    public Boolean hasChain(SendMessage s) {
        return actions.containsKey(s.getText());
    }

    public SendMessage processChain(SendMessage s, Update update) {
        if (hasChain(s)) {
            update.getMessage().setText(s.getText());
            return updateReceived(update);
        }
        return s;
    }

    public String getChatId(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        return update.getMessage().getChatId().toString();
    }

    public String getMessageText(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }
        return update.getMessage().getText();
    }

    //todo после того как вылетело исключение
    // в течение работы с бд, вызвать этот
    // метод чтобы предыдущую команду запустить заново

    public SendMessage forceEvokePreviousCommand(Update update) {
        String chatId = getChatId(update);
        return actions.get(bindingBy.get(chatId)).handle(update);
    }
}
