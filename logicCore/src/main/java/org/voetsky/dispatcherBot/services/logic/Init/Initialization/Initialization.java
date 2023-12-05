package org.voetsky.dispatcherBot.services.logic.Init.Initialization;


import org.voetsky.dispatcherBot.services.logic.commands.command.Command;
import java.util.Map;

public interface Initialization {
    Map<String, Command> initCommands();
}
