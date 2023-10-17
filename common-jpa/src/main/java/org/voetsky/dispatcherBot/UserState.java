package org.voetsky.dispatcherBot;

public enum UserState {

    // 2 state: awaiting for text, command state, awaiting for doc, awaiting for mp3,
    // в стейте awaiting нельзя ввести команду и запустится предыдущая. В стейте command
    // команда вводится но нельзя ввести кастом ввод, запуск пред команды
    AWAITING_FOR_TEXT,
    AWAITING_FOR_COMMAND,
    AWAITING_FOR_AUDIO,
    AWAITING_FOR_BUTTON,
    AWAITING_FOR_VOICE;
}
