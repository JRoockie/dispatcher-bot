package org.voetsky.dispatcherBot.configuration.LogicCoreLocalization;

import javax.annotation.PostConstruct;
import java.io.IOException;

public interface LogicCoreLocalization {

    @PostConstruct
    void loadDic(String lang) throws IOException;

    String get(String lang, String key);

    String getKeyFromDefaultLang(String key);

    String getUnknownKeyByDefaultLang();
}
