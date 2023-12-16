package org.voetsky.dispatcherBot.configuration.Initialization;


import org.voetsky.dispatcherBot.services.logic.commands.command.Command;

import java.util.Map;

public interface Initialization {
    Map<String, Command> initCommands();
}
