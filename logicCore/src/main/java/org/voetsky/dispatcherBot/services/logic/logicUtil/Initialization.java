package org.voetsky.dispatcherBot.services.logic.logicUtil;


import org.voetsky.dispatcherBot.services.logic.commands.command.CommandInterface;
import java.util.Map;

public interface Initialization {
    Map<String, CommandInterface> initCommands();
}
