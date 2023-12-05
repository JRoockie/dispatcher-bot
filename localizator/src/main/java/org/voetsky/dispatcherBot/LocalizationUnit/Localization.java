package org.voetsky.dispatcherBot.LocalizationUnit;

import java.io.IOException;

public interface Localization {

    void loadDic(String lang) throws IOException;

    String get(String lang, String key);

}
