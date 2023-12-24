package org.voetsky.dispatcherBot.service.dispatcherLocalization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.voetsky.dispatcherBot.configuration.dispatcherLocalization.DispatcherLangUnit;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class DispatcherLangUnitTest {

    private DispatcherLangUnit dispatcherLangUnit;

    @BeforeEach
    void setUp() {
        dispatcherLangUnit = new DispatcherLangUnit();
        try {
            dispatcherLangUnit.initLocalizations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetExistingKey() {
        String lang = "ru";
        String key = "test.key";
        String expectedValue = "русский";
        String actualValue = dispatcherLangUnit.get(lang, key);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void testGetNonExistingKey() {
        String lang = "ru";
        String key = "unknown";
        String defaultValue = "Несуществующий ключ для данных";
        String actualValue = dispatcherLangUnit.get(lang, key);

        assertEquals(defaultValue, actualValue);
    }

    @Test
    void testGetDefault() {
        String key = "type.error";
        String expectedValue = "Неподдерживаемый тип сообщения!";
        String actualValue = dispatcherLangUnit.getKeyFromDefaultLang(key);

        assertEquals(expectedValue, actualValue);
    }
}