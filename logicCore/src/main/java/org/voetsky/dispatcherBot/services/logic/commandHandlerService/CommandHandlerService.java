package org.voetsky.dispatcherBot.services.logic.commandHandlerService;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.exceptions.ServiceException;
import org.voetsky.dispatcherBot.services.logic.commandInitialization.CommandInit;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j
@Getter
@Component
public class CommandHandlerService implements CommandHandler {

    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageMaker messageMaker;
    private Map<String, Command> actions;
    private final CommandInit commandInit;

    public CommandHandlerService(MessageMaker messageMaker, CommandInit commandInit) {
        this.messageMaker = messageMaker;
        this.commandInit = commandInit;
    }

    @PostConstruct
    @Override
    public void initCommands() {
        actions = commandInit.initCommands();
    }

    @Override
    public SendMessage updateReceived(Update update) {
        var text = getMessageText(update);
        var chatId = getChatId(update);
        try {
            if (update.hasMessage() && update.getMessage().getText() != null) {
                return processCommand(update, text, chatId);
            } else if (update.hasCallbackQuery()) {
                if (hasForceCallback(text, update)) {
                    return forceCallback(update, text);
                }
                return processButton(update, text, chatId);
            } else if (update.getMessage().hasAudio() || update.getMessage().hasVoice()) {
                return processCallBack(update, chatId);
            }
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()){
                log.error("FATAL ERROR:", e);
            }
            messageMaker.makeSendMessage(update, "fatal.err", true);
        }
        throw new LogicCoreException(
                messageMaker.getTextFromProperties(update, "fatal.err"));
    }

    @Override
    public boolean hasForceCallback(String text, Update update) {

        if (text.startsWith("*")) {
            if (log.isDebugEnabled()) {
                log.debug("FORCE CALLBACK: true");
            }
            return true;
        } else if (update.hasCallbackQuery()) {
            String s = update.getCallbackQuery().getData();
            if (s.startsWith("*")) {
                if (log.isDebugEnabled()) {
                    log.debug("FORCE CALLBACK: true");
                }
                return true;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("FORCE CALLBACK: false");
        }
        return false;
    }

    @Override
    public SendMessage forceCallback(Update update, String text) {
        text = text.replace("*", "");
        if (log.isDebugEnabled()) {
            log.debug(String.format("FORCE CALLBACK command part: %s",
                    actions.get(text)));
        }
        try {
            var msg = actions.get(text).callback(update);
            return processChain(msg, update);
        } catch (NullPointerException e) {
            throw new ServiceException("Команды не существует");
        }
    }

    @Override
    public SendMessage processCommand(Update update, String text, String chatId) {
        if (actions.containsKey(text)) {
            return processHandle(update, text, chatId);
        } else if (bindingBy.containsKey(chatId)) {
            return processCallBack(update, chatId);
        }
        throw new ServiceException("tech.err.command");
    }

    @Override
    public SendMessage processHandle(Update update, String text, String chatId) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("NON-CALLBACK command part: %s",
                    actions.get(text)));
        }
        try {
            var msg = actions.get(text).handle(update);
            bindingBy.put(chatId, text);
            return processChain(msg, update);
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()){
                log.error("FATAL ERROR:", e);
            }
            throw new ServiceException("tech.err.commandDoesNotExist");
        }
    }

    @Override
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
        if (actions.get(text) == null) {
            return processCallBack(update, chatId);
        }
        return processHandle(update, text, chatId);
    }

    @Override
    public Boolean hasChain(SendMessage s) {
        return actions.containsKey(s.getText());
    }

    @Override
    public SendMessage processChain(SendMessage s, Update update) {
        String text = s.getText();
        if (hasChain(s)) {
            if (update.hasMessage()) {
                update.getMessage().setText(text);
                update.setCallbackQuery(null);
            } else if (update.hasCallbackQuery()) {
                Message message = new Message();
                message.setText(text);
                message.setChat(update.getCallbackQuery().getMessage().getChat());
                message.setFrom(update.getCallbackQuery().getFrom());
                update.setMessage(message);
                update.setCallbackQuery(null);
            }
            return updateReceived(update);
        }
        return s;
    }

    @Override
    public String getChatId(Update update) {
        return update.hasCallbackQuery() ?
                update.getCallbackQuery().getMessage().getChatId().toString() :
                update.getMessage().getChatId().toString();
    }

    @Override
    public String getMessageText(Update update) {
        return update.hasCallbackQuery() ? update.getCallbackQuery().getData() : update.getMessage().getText();
    }

}
