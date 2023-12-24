package org.voetsky.dispatcherBot.configuration.dispatcherLocalization;

import javax.annotation.PostConstruct;
import java.io.IOException;

public interface DispatcherLang {

    @PostConstruct
    void loadDic(String lang) throws IOException;

    String get(String lang, String key);

    String getDefaultLocalValue(String key);

    String getUnknownKey(String lang);

}
