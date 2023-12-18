package org.voetsky.dispatcherBot.configuration.LogicCoreLocalization;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.LogicCoreApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Log4j
@Service
public class LogicCoreLocalizationUnit implements LogicCoreLocalization {
    private final Map<String, Map<String, String>> dic = new HashMap<>();
    private final String DEFAULT_LANG = "ru";

    @PostConstruct
    @Override
    public void initLocalizations() throws IOException {
        this.loadDic("ru");
        this.loadDic("kk");
        if (log.isDebugEnabled()){
            log.debug("LogicCore: ALL languages loaded");
        }
    }

    @Override
    public void loadDic(String lang) throws IOException {
        if (log.isDebugEnabled()){
            log.debug(String.format("LogicCore: Loading language %S", lang));
        }
        var prop = new Properties();
        try (var langPr = LogicCoreApplication.class.getClassLoader()
                .getResourceAsStream(String.format("lang_%s.properties", lang))) {
            prop.load(new InputStreamReader(
                    Objects.requireNonNull(langPr), StandardCharsets.UTF_8));
        }
        var map = new HashMap<String, String>();
        for (var name : prop.keySet()) {
            map.put(name.toString(), prop.getProperty(name.toString()));
        }
        dic.put(lang, map);
        if (log.isDebugEnabled()){
            log.debug(String.format("LogicCore: Language %S loaded!", lang));
        }
    }

    @Override
    public String get(String lang, String key) {
        try {
            return dic.get(lang).get(key);
        } catch (NullPointerException e){
            if (log.isDebugEnabled()){
                log.error(String.format(
                        "LogicCore: Language not found %S", lang));
            }
            return getDefault(key);
        }
    }

    public String getDefault(String key) {
        if (log.isDebugEnabled()){
            log.debug(String.format(
                    "LogicCore: Getting default lang value %S",
                    DEFAULT_LANG));
        }
        return dic.get(DEFAULT_LANG).get(key);
    }

}