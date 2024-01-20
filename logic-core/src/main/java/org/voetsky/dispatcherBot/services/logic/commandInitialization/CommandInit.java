package org.voetsky.dispatcherBot.services.logic.commandInitialization;


import org.voetsky.dispatcherBot.services.logic.commands.command.Command;

import java.util.Map;

public interface CommandInit {

    Map<String, Command> initCommands();

}
