package org.voetsky.dispatcherBot.commands;


import org.voetsky.dispatcherBot.commands.impl.*;

import java.util.Map;

public enum Commands {

    START_COMMAND("/start"),
    ASK_NAME_COMMAND("/askNameCommand"),
    SONG_ADD_AND_ADD_SONG_NAME_COMMAND("/songAddAndSongNameCommand"),
    CHOOSING_NAME_OR_ANOTHER_WAY("/choosingNameOrAnotherWay"),
    MP3_ADD_COMMAND("/mp3AddCommand"),
    VOICE_ADD_COMMAND("/voiceAddCommand"),

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

