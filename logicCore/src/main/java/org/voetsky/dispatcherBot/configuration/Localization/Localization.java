package org.voetsky.dispatcherBot.configuration.Localization;

import java.io.IOException;

public interface Localization {

    void loadDic(String lang) throws IOException;

    String get(String lang, String key);

}
