package org.voetsky.dispatcherBot.service.dispatcherLocalization;

import javax.annotation.PostConstruct;
import java.io.IOException;

public interface DispatcherLang {

    @PostConstruct
    void loadDic(String lang) throws IOException;

    String get(String lang, String key);

    String getDefault(String key);

}
