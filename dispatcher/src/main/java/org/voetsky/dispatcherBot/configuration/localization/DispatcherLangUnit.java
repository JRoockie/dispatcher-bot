package org.voetsky.dispatcherBot.configuration.localization;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.DispatcherApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Log4j
@Service
public class DispatcherLangUnit implements DispatcherLang {

    private final Map<String, Map<String, String>> dic = new HashMap<>();
    private final String DEFAULT_LANG = "ru";
    private final String UNKNOWN_KEY = "unknown.key";

    @PostConstruct
    public void initLocalizations() throws IOException {
        this.loadDic("ru");
        this.loadDic("kk");
        if (log.isDebugEnabled()) {
            log.debug("Dispatcher: ALL languages loaded");
        }
    }

    @Override
    public void loadDic(String lang) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Dispatcher: Loading language %S", lang));
        }
        var prop = new Properties();

        try (var langPr = DispatcherApplication.class.getClassLoader()
                .getResourceAsStream(String.format("lang_%s.properties", lang))) {
            prop.load(new InputStreamReader(
                    Objects.requireNonNull(langPr), StandardCharsets.UTF_8));
        }
        var map = new HashMap<String, String>();
        prop.stringPropertyNames().forEach(name -> map.put(name, prop.getProperty(name)));

        dic.put(lang, map);
        if (log.isDebugEnabled()) {
            log.debug(String.format("Dispatcher: Language %S loaded!", lang));
        }
    }

    @Override
    public String get(String lang, String key) {
        var value = hasLangValue(lang, key);

        if (value != null) {
            return value;
        }
        if (log.isDebugEnabled()) {
            log.error(String.format(
                    "LogicCore: Language not found %S", lang));
        }
        return getKeyFromDefaultLang(key);

    }

    private String hasLangValue(String lang, String key) {
        return dic.get(lang) != null ? dic.get(lang).get(key) : null;
    }

    private String hasDefaultLangValue(String key) {
        return dic.get(DEFAULT_LANG) != null ? dic.get(DEFAULT_LANG).get(key) : null;
    }

    @Override
    public String getKeyFromDefaultLang(String key) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("LogicCore: Getting default lang value %S", DEFAULT_LANG));
        }

        String value = hasDefaultLangValue(key);

        if (value != null) {
            return value;
        }
        if (log.isDebugEnabled()) {
            log.error(String.format("Dispatcher: Key not found %S", key));
        }

        return getUnknownKeyByDefaultLang();
    }

    @Override
    public String getUnknownKeyByDefaultLang() {
        if (log.isDebugEnabled()) {
            log.debug(String.format(
                    "Dispatcher: Getting unknown value %S", DEFAULT_LANG));
        }

        return dic.get(DEFAULT_LANG).get(UNKNOWN_KEY);
    }

}