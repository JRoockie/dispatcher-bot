package org.voetsky.dispatcherBot.services.logic.commandInitialization;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.services.logic.commands.*;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMaker;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainRepo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.*;

@AllArgsConstructor
@Service
public class CommandInitService implements CommandInit {
    private final MainRepo mainRepoService;
    private final MessageMaker messageMaker;

    @PostConstruct
    @Override
    public Map<String, Command> initCommands() {
        Map<String, Command> actions = new HashMap<>();
        actions.put(START_COMMAND.toString(), new Start(mainRepoService, messageMaker));
        actions.put(CLIENT_NAME.toString(), new ClientName(mainRepoService, messageMaker));
        actions.put(SONG_NAME_OR_MP3.toString(), new SongNameOrMp3(mainRepoService, messageMaker));
        actions.put(SONG_NAME.toString(), new SongName(mainRepoService, messageMaker));
        actions.put(MP3_ADD.toString(), new Mp3Add(mainRepoService, messageMaker));
        actions.put(ADD_LINK.toString(), new AddLink(mainRepoService, messageMaker));
        actions.put(HOW_MUCH_PEOPLE.toString(), new HowMuchPeople(mainRepoService, messageMaker));
        actions.put(WHO_WILL_SING.toString(), new WhoWillSing(mainRepoService, messageMaker));
        actions.put(VOICE_ADD.toString(), new VoiceAdd(mainRepoService, messageMaker));
        actions.put(SHOW_PRICE.toString(), new ShowPrice(mainRepoService, messageMaker));
        actions.put(ADD_NUMBER.toString(), new AddNumber(mainRepoService, messageMaker));
        actions.put(ADD_COMMENT.toString(), new AddComment(mainRepoService, messageMaker));
        actions.put(FINISH.toString(), new Finish(mainRepoService, messageMaker));
        return actions;
    }
}
