package org.voetsky.dispatcherBot.services.logic.commands.command;



public enum Commands {


    ASK_NAME_COMMAND("/askNameCommand"),
    SONG_ADD_AND_ADD_SONG_NAME_COMMAND("/songAddAndSongNameCommand"),
    CHOOSING_NAME_OR_ANOTHER_WAY("/choosingNameOrAnotherWay"),
    MP3_ADD_COMMAND("/mp3AddCommand"),
    VOICE_ADD_COMMAND("/voiceAddCommand"),

    START_COMMAND("/start"),
    CLIENT_NAME_COMMAND("/clientNameCommand"),
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

