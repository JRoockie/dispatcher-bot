package org.voetsky.dispatcherBot.commandLogic.commandHandlerService;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.commands.*;
import org.voetsky.dispatcherBot.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.repoAcess.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.voetsky.dispatcherBot.commands.command.Commands.*;

@Log4j
@Getter
@Component
public class CommandHandlerService implements CommandHandler {

    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final MessageMakerService messageMakerService;
    private Map<String, CommandInterface> actions;

    //todo ТУТ ЕГО ЮЫТЬ НЕ ДОЛЖНО, ПЕРЕКИНУТЬ repocontroller в ДРУГОЙ СЕРВИС
    //О нем не должен знать CommandHandler
    //также перекинуть метод INIT И инитить команды в другом месте
    private final RepoController repoController;

    public CommandHandlerService(MessageMakerService messageMakerService, RepoController repoController) {
        this.messageMakerService = messageMakerService;
        this.repoController = repoController;
//        init();
    }

    @PostConstruct
    public void init() {
        actions = Map.of(
                START_COMMAND.toString(), new StartCommand(repoController, messageMakerService),
                ASK_NAME_COMMAND.toString(), new AskNameCommand(repoController, messageMakerService),
                SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString(), new SongAddAndSongNameCommand(repoController, messageMakerService),
                CHOOSING_NAME_OR_ANOTHER_WAY.toString(), new ChoosingNameOrAnotherWayCommand(repoController, messageMakerService),
                MP3_ADD_COMMAND.toString(), new Mp3AddCommand(repoController, messageMakerService),
                VOICE_ADD_COMMAND.toString(), new VoiceAddCommand(repoController, messageMakerService));
    }

    public SendMessage updateReceived(Update update) {

        log.debug("CONTROLLER: Choosing command");
        if (update.hasMessage() && update.getMessage().getText() != null) {
            var key = update.getMessage().getText();
            var chatId = update.getMessage().getChatId().toString();
            if (actions.containsKey(key)) {
                log.debug("CONTROLLER: Executing NON-CALLBACK command part: " + actions.get(key));
                var msg = actions.get(key).handle(update);
                bindingBy.put(chatId, key);
                return processChain(msg, update);
            } else if (bindingBy.containsKey(chatId)) {
                var msg = actions.get(bindingBy.get(chatId)).callback(update);
                log.debug("CONTROLLER: Executing CALLBACK command part : "
                        + bindingBy.get(chatId));
                return processChain(msg, update);
            }

        } else if (update.hasCallbackQuery()) {
            log.debug("CONTROLLER: Executing BUTTON ");
            return buttonExecute(update);

        } else if (update.getMessage().hasAudio()) {
            var chatId = update.getMessage().getChatId().toString();
            var msg = actions.get(bindingBy.get(chatId)).callback(update);
            log.debug("CONTROLLER: Executing CALLBACK command part : "
                    + bindingBy.get(chatId));
            return processChain(msg, update);

        } else if (update.getMessage().hasVoice()) {
            var chatId = update.getMessage().getChatId().toString();
            var msg = actions.get(bindingBy.get(chatId)).callback(update);
            log.debug("CONTROLLER: Executing CALLBACK command part : "
                    + bindingBy.get(chatId));
            return processChain(msg, update);
        }
        log.debug("Callback doesn't found, command not found");
        return messageMakerService.makeSendMessage(update, "Command not found, callback not found.");
    }

    //todo переписать
    public SendMessage buttonExecute(Update update) {
        try {
            User user = update.getCallbackQuery().getFrom();
            String callBack = update.getCallbackQuery().getData();
            log.debug("CONTROLLER: Button has pressed by: " + user.getId() + " with attribute: " + callBack);
            if (actions.containsKey(callBack)) {
                var msg = actions.get(callBack).handle(update);
                bindingBy.put(update.getCallbackQuery().getFrom().getId().toString(), callBack);
                return processChain(msg, update);
            } else {
                throw new RuntimeException("CONTROLLER: Button command not found");
            }
        } catch (RuntimeException e) {
            return messageMakerService.makeSendMessage(update, "Ошибка нажатия кнопки. Введите /start ");
        }
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

}
