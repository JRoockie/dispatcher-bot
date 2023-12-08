package org.voetsky.dispatcherBot.configuration.Initialization;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.logic.commands.newCommands.*;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repo.RepoController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@AllArgsConstructor
@Service
public class CommandInit implements Initialization {
    private final RepoController repoController;
    private final MessageMakerService messageMakerService;

    @PostConstruct
    @Override
    public Map<String, Command> initCommands() {
        Map<String, Command> actions = new HashMap<>();
//        actions = Map.of(
//                START_COMMAND.toString(), new StartCommand(repoController, messageMakerService),
//                ASK_NAME_COMMAND.toString(), new AskNameCommand(repoController, messageMakerService),
//                SONG_ADD_AND_ADD_SONG_NAME_COMMAND.toString(), new SongAddAndSongNameCommand(repoController, messageMakerService),
//                CHOOSING_NAME_OR_ANOTHER_WAY.toString(), new ChoosingNameOrAnotherWayCommand(repoController, messageMakerService),
//                MP3_ADD_COMMAND.toString(), new Mp3AddCommand(repoController, messageMakerService),
//                VOICE_ADD_COMMAND.toString(), new VoiceAddCommand(repoController, messageMakerService));
//        return actions;
        actions.put(START_COMMAND.toString(), new StartCommand(repoController, messageMakerService));
        actions.put(CLIENT_NAME_COMMAND.toString(), new ClientNameCommand(repoController, messageMakerService));
        actions.put(SONG_NAME_OR_MP3.toString(), new SongNameOrMp3(repoController, messageMakerService));
        actions.put(SONG_NAME.toString(), new SongName(repoController, messageMakerService));
        actions.put(MP3_ADD_COMMAND.toString(), new Mp3AddCommand(repoController, messageMakerService));
        actions.put(ADD_LINK.toString(), new AddLink(repoController, messageMakerService));
        actions.put(HOW_MUCH_PEOPLE.toString(), new HowMuchPeople(repoController, messageMakerService));
        actions.put(WHO_WILL_SING.toString(), new WhoWillSing(repoController, messageMakerService));
        actions.put(VOICE_ADD_COMMAND.toString(), new VoiceAddCommand(repoController, messageMakerService));
        actions.put(SHOW_PRICE.toString(), new ShowPrice(repoController, messageMakerService));
        actions.put(ADD_NUMBER.toString(), new AddNumber(repoController, messageMakerService));
        actions.put(ADD_COMMENT.toString(), new AddComment(repoController, messageMakerService));
        actions.put(FINISH.toString(), new Finish(repoController, messageMakerService));
        return actions;
    }
}
