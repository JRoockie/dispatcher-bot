package org.voetsky.dispatcherBot.services.logic.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.command.Commands;
import org.voetsky.dispatcherBot.services.repo.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import static org.voetsky.dispatcherBot.UserState.*;

@Log4j
@AllArgsConstructor
public class Mp3AddCommand implements Command {

    private final String action = Commands.MP3_ADD_COMMAND.toString();

    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = "Отправьте песню mp3 файлом";
        changeState(update, AWAITING_FOR_AUDIO);
        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String text = "Успешное добавление аудио в бд";
        changeState(update, AWAITING_FOR_COMMAND);
        repoController.addMp3(update);
       return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }
}
