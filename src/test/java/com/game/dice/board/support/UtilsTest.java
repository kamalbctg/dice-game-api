package com.game.dice.board.support;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    private final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    @Test
    void testUtilConstruction() {
        assertThrows(InvocationTargetException.class, () -> {
            Constructor<Utils> constructor = Utils.class.getDeclaredConstructor();
            constructor.setAccessible(true); // Allow private access
            constructor.newInstance();
        });
    }

    @Test
    void testUuid() {
        String uuid = Utils.uuid();
        assertNotNull(uuid);
        assertTrue(isValidUuid(uuid));
    }

    public boolean isValidUuid(String uuid) {
        Matcher matcher = UUID_PATTERN.matcher(uuid);
        return matcher.matches();
    }
}