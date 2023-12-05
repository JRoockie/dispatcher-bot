package org.voetsky.dispatcherBot.configuration.Localization;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.voetsky.dispatcherBot.LogicCoreApplication;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j
@Component
public class LocalizationUnit implements Localization {
    private final Map<String, Map<String, String>> dic = new HashMap<>();

    public void loadDic(String lang) throws IOException {
        var prop = new Properties();
        try (var langPr = LogicCoreApplication.class.getClassLoader()
                .getResourceAsStream(String.format("lang_%s.properties", lang))) {
            prop.load(langPr);
        }
        var map = new HashMap<String, String>();
        for (var name : prop.keySet()) {
            map.put(name.toString(), prop.getProperty(name.toString()));
        }
        dic.put(lang, map);
    }

    public String get(String lang, String key) {
        return dic.get(lang).get(key);
    }

}