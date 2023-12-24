package logic.commandHandlerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.configuration.Initialization.CommandInit;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandlerService;
import org.voetsky.dispatcherBot.services.logic.commands.Start;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainRepoService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.START_COMMAND;

@RunWith(SpringRunner.class)
class CommandHandlerServiceTest {

    @InjectMocks
    private MessageMakerService mockMessageMaker;

    @Mock
    private MainRepoService mainRepoService;

    @Mock
    private CommandInit mockInitialization;

    @InjectMocks
    private CommandHandlerService commandHandlerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockInitialization.initCommands()).thenReturn(createMockCommands());
        commandHandlerService.initCommands();
    }

    private Map<String, Command> createMockCommands() {
        Map<String, Command> commandsMap = new HashMap<>();
        commandsMap.put(START_COMMAND.toString(), new Start(mainRepoService, mockMessageMaker));
        return commandsMap;
    }

    void testUpdateReceivedWithValidCommand() {
        Update mockUpdate = mock(Update.class);
        when(mockUpdate.hasMessage()).thenReturn(true);
        when(mockUpdate.getUpdateId()).thenReturn(123);

        Message mockMessage = mock(Message.class);

        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockUpdate.getMessage().getText()).thenReturn("/start");
        when(mockMessage.getText()).thenReturn("/start");

        SendMessage result = commandHandlerService.updateReceived(mockUpdate);

        assertNotNull(result);
    }

    @Test
    void testUpdateReceivedWithInvalidCommand() {
        Update mockUpdate = mock(Update.class);
        when(mockUpdate.hasMessage()).thenReturn(true);
        Message mockMessage = mock(Message.class);
        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockMessage.getText()).thenReturn("/invalid_command");

        assertThrows(LogicCoreException.class, () -> commandHandlerService.updateReceived(mockUpdate));
    }
}

