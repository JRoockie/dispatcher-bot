package org.voetsky.dispatcherBot.commands;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.repoAcess.RepoController;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;

import static org.voetsky.dispatcherBot.UserState.*;
import static org.voetsky.dispatcherBot.commands.command.Commands.VOICE_ADD_COMMAND;

@Log4j
@AllArgsConstructor
public class VoiceAddCommand implements CommandInterface {

    private final String action = VOICE_ADD_COMMAND.toString();
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @Override
    public SendMessage handle(Update update) {
        String text = "Отправьте голосовое сообщение";
        changeState(update, AWAITING_FOR_VOICE);

        return messageMakerService.makeSendMessage(update, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String text = "Успешное добавление ГС в бд";
        changeState(update, AWAITING_FOR_COMMAND);
        repoController.addVoice(update);

        return messageMakerService.makeSendMessage(
                update, text);
    }

    @Override
    public void changeState(Update update, UserState userState) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("State changed to %s", AWAITING_FOR_BUTTON));
        }
        repoController.setUserState(update, userState);
    }
}
