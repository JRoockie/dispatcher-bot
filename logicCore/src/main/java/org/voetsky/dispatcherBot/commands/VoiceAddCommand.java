package org.voetsky.dispatcherBot.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.controller.RepoController;
import org.voetsky.dispatcherBot.services.messageMakerService.MessageMakerService;

import java.util.HashMap;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.command.Commands.VOICE_ADD_COMMAND;

@Log4j
@AllArgsConstructor
public class VoiceAddCommand implements CommandInterface {

    private final String action = VOICE_ADD_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public HashMap<Boolean, SendMessage> handle(Update update) {
        changeState(update, AWAITING_FOR_VOICE);
        return messageMakerService.makeMap(update, "Отправьте голосовое сообщение");
    }

    @Override
    public HashMap<Boolean, SendMessage> callback(Update update) {
        changeState(update, AWAITING_FOR_COMMAND);
        repoController.addVoice(update);
        return messageMakerService.makeMap(update, "Успешное добавление ГС в бд");
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }
}
