package org.voetsky.dispatcherBot.services.logic.commands.command;

public enum Commands {

    START_COMMAND("/start"),
    CLIENT_NAME("/clientName"),
    SONG_NAME_OR_MP3("/songNameOrMp3"),
    SONG_NAME("/songName"),
    LINK_OR_MP3("/linkOrMp3"),
    ADD_LINK("/addLink"),
    HOW_MUCH_PEOPLE("/howMuchPeople"),
    WHO_WILL_SING("/whoWillSing"),
    SHOW_PRICE("/showPrice"),
    ADD_NUMBER("/addNumber"),
    ADD_COMMENT("/addComment"),
    FINISH("/finish"),
    MP3_ADD("/mp3Add"),
    VOICE_ADD("/voiceAdd");

    private final String command;

    Commands(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

}

