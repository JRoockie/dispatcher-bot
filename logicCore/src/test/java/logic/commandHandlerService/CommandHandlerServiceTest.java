package logic.commandHandlerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voetsky.dispatcherBot.configuration.Initialization.CommandInit;
import org.voetsky.dispatcherBot.configuration.LogicCoreLocalization.LogicCoreLocalization;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandlerService;
import org.voetsky.dispatcherBot.services.logic.commands.Finish;
import org.voetsky.dispatcherBot.services.logic.commands.Start;
import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.repoServices.mainRepoService.MainRepoService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.FINISH;
import static org.voetsky.dispatcherBot.services.logic.commands.command.Commands.START_COMMAND;

@RunWith(SpringRunner.class)
class CommandHandlerServiceTest {

    @InjectMocks
    private MessageMakerService mockMessageMaker;

    @Mock
    private MainRepoService mainRepoService;

    @Mock
    private CommandInit mockInitialization;

    @Mock
    private LogicCoreLocalization localization;

    @Spy
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
        commandsMap.put(FINISH.toString(), new Finish(mainRepoService, mockMessageMaker));
        return commandsMap;
    }

    @Test
    void testUpdateReceivedWithValidCommand() {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = mock(Message.class);
        User mockUser = mock(User.class);

        when(mockUpdate.hasMessage()).thenReturn(true);
        when(mockUpdate.getUpdateId()).thenReturn(123);
        mockUser.setLanguageCode("ru");
        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockUpdate.getMessage().getFrom()).thenReturn(mockUser);
        when(mockUpdate.getMessage().getText()).thenReturn("/start");
        when(mockMessage.getText()).thenReturn("/start");
        when(mockUpdate.getMessage().getFrom().getLanguageCode()).thenReturn("ru");

        SendMessage result = commandHandlerService.updateReceived(mockUpdate);

        assertNotNull(result);
    }

    @Test
    void testUpdateReceivedWithInvalidCommand() {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);

        when(mockUpdate.hasMessage()).thenReturn(true);
        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockMessage.getText()).thenReturn("/invalid_command");

        assertThrows(LogicCoreException.class, () -> commandHandlerService.updateReceived(mockUpdate));
    }

    @Test
    public void testForceCallback() {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);
        String falseText = "/false";

        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockUpdate.getMessage().getFrom()).thenReturn(mockUser);
        when(mockMessage.getText()).thenReturn("*/start");
        when(mockUpdate.getMessage().getFrom().getLanguageCode()).thenReturn("ru");

        commandHandlerService.forceCallback(mockUpdate, "*/start");

        assertThrows(LogicCoreException.class, () -> commandHandlerService.forceCallback(mockUpdate, falseText));

        verify(commandHandlerService).processChain(any(), eq(mockUpdate));

    }

    @Test
    public void testHasForceCallback() {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);
        String falseText = "/false";

        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockUpdate.getMessage().getFrom()).thenReturn(mockUser);
        when(mockMessage.getText()).thenReturn("*/start");
        when(mockUpdate.getMessage().getFrom().getLanguageCode()).thenReturn("ru");

        Boolean resultFalse = commandHandlerService.hasForceCallback(falseText, mockUpdate);
        Boolean resultTrue = commandHandlerService.hasForceCallback("*/start", mockUpdate);

        assertEquals(true, resultTrue);
        assertEquals(false, resultFalse);
    }

    @Test
    public void testProcessCommand() {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);
        String trueText = "/start";
        String falseText = "/false";
        String chatId = "123";
        String chatId2 = "000";

        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockUpdate.getMessage().getFrom()).thenReturn(mockUser);
        when(mockMessage.getText()).thenReturn("/start");
        when(mockUpdate.getMessage().getFrom().getLanguageCode()).thenReturn("ru");

        commandHandlerService.processCommand(mockUpdate, trueText, chatId);
        verify(commandHandlerService).processCommand(mockUpdate, trueText, chatId);

        assertThrows(LogicCoreException.class, () -> commandHandlerService.processCommand(mockUpdate, falseText, chatId2));

    }

    @Test
    public void testProcessHandle() {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);
        User mockUser = Mockito.mock(User.class);
        String trueText = "/start";
        String falseText = "/q123";
        String chatId = "123";
        String chatIdFalse = "000";

        when(mockUpdate.getMessage()).thenReturn(mockMessage);
        when(mockUpdate.getMessage().getFrom()).thenReturn(mockUser);
        when(mockMessage.getText()).thenReturn("/start");
        when(mockUpdate.getMessage().getFrom().getLanguageCode()).thenReturn("ru");

        commandHandlerService.processCommand(mockUpdate, trueText, chatId);
        commandHandlerService.processHandle(mockUpdate, trueText, chatId);

        verify(commandHandlerService, times(2)).processChain(any(), eq(mockUpdate));
        assertThrows(LogicCoreException.class, () -> commandHandlerService.processCommand(mockUpdate, falseText, chatIdFalse));

    }

}

