package org.voetsky.dispatcherBot.services.logic.logicUtil;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.voetsky.dispatcherBot.services.logic.commands.*;
import org.voetsky.dispatcherBot.services.logic.commands.command.CommandInterface;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import javax.annotation.PostConstruct;
import java.util.Map;

import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.VOICE_ADD_COMMAND;

@AllArgsConstructor
@Component
public class CommandInit implements Initialization {
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @PostConstruct
    @Override
    public Map<String, CommandInterface> initCommands() {
        Map<String, CommandInterface> actions;
        actions = Map.of(
                START_COMMAND.toString(), new StartCommand(repoController, messageMakerService),
                ASK_NAME_COMMAND.toString(), new AskNameCommand(repoController, messageMakerService),
                SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString(), new SongAddAndSongNameCommand(repoController, messageMakerService),
                CHOOSING_NAME_OR_ANOTHER_WAY.toString(), new ChoosingNameOrAnotherWayCommand(repoController, messageMakerService),
                MP3_ADD_COMMAND.toString(), new Mp3AddCommand(repoController, messageMakerService),
                VOICE_ADD_COMMAND.toString(), new VoiceAddCommand(repoController, messageMakerService));
        return actions;
    }
}
