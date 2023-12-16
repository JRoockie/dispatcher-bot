package org.voetsky.dispatcherBot.configuration.Initialization;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.services.logic.commands.*;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@AllArgsConstructor
@Service
public class CommandInit implements Initialization {
    private final MainService mainRepoService;
    private final MessageMakerService messageMakerService;

    @PostConstruct
    @Override
    public Map<String, Command> initCommands() {
        Map<String, Command> actions = new HashMap<>();
        actions.put(START_COMMAND.toString(), new Start(mainRepoService, messageMakerService));
        actions.put(CLIENT_NAME.toString(), new ClientName(mainRepoService, messageMakerService));
        actions.put(SONG_NAME_OR_MP3.toString(), new SongNameOrMp3(mainRepoService, messageMakerService));
        actions.put(SONG_NAME.toString(), new SongName(mainRepoService, messageMakerService));
        actions.put(MP3_ADD.toString(), new Mp3Add(mainRepoService, messageMakerService));
        actions.put(ADD_LINK.toString(), new AddLink(mainRepoService, messageMakerService));
        actions.put(HOW_MUCH_PEOPLE.toString(), new HowMuchPeople(mainRepoService, messageMakerService));
        actions.put(WHO_WILL_SING.toString(), new WhoWillSing(mainRepoService, messageMakerService));
        actions.put(VOICE_ADD.toString(), new VoiceAdd(mainRepoService, messageMakerService));
        actions.put(SHOW_PRICE.toString(), new ShowPrice(mainRepoService, messageMakerService));
        actions.put(ADD_NUMBER.toString(), new AddNumber(mainRepoService, messageMakerService));
        actions.put(ADD_COMMENT.toString(), new AddComment(mainRepoService, messageMakerService));
        actions.put(FINISH.toString(), new Finish(mainRepoService, messageMakerService));
        return actions;
    }
}
