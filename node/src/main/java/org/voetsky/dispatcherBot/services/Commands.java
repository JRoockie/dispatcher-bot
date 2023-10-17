package org.voetsky.dispatcherBot.services;

public enum Commands {

    START_COMMAND("/startCommand"),
    SONG_NAME_COMMAND("/songNameCommand"),
    SONG_ADD_COMMAND("/songAddCommand");

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
