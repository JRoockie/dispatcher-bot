package org.voetsky.dispatcherBot;

public enum UserState {
    AWAITING_FOR_TEXT,
    AWAITING_FOR_COMMAND,
    AWAITING_FOR_AUDIO,
    AWAITING_FOR_BUTTON,
    AWAITING_FOR_VOICE,
    INVALID_SESSION
}
