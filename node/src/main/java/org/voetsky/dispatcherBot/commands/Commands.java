package org.voetsky.dispatcherBot.commands;


import org.voetsky.dispatcherBot.commands.impl.*;

import java.util.Map;

public enum Commands {

    START_COMMAND("/start"),
    ASK_NAME_COMMAND("/askNameCommand"),
    SONG_ADD_AND_ADD_SONG_NAME_COMMAND("/songAddAndSongNameCommand"),
    CHOOSING_NAME_OR_ANOTHER_WAY("/choosingNameOrAnotherWay"),

    ;

    private final String command;

    Commands(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public boolean equals(String command){
        return this.toString().equals(command);
    }

}

