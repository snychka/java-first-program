package com.h2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.ReflectionUtils.isPrivate;
import static org.junit.platform.commons.util.ReflectionUtils.tryToLoadClass;

public class Module3_Test {
    private final String classToFind = "com.h2.SavingsCalculator";
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private static Optional<Class<?>> getClass(final String className) {
        Try<Class<?>> aClass = tryToLoadClass(className);
        return aClass.toOptional();
    }

    public Optional<Class<?>> getSavingsClass() {
        return getClass(classToFind);
    }

    @Test
    public void m03_01_testSavingsCalculatorExists() {
        Optional<Class<?>> maybeClass = getSavingsClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must be created.");
    }

    @Test
    public void m3_02_testExistenceOfPrivateFields() {
        final Optional<Class<?>> maybeSavingsCalculator = getSavingsClass();
        assertTrue(maybeSavingsCalculator.isPresent(), classToFind + " must exist");
        final Class<?> savingsCalculator = maybeSavingsCalculator.get();
        final Set<String> fieldNames = new HashSet<>(Arrays.asList("credits", "debits"));

        final Field[] declaredFields = savingsCalculator.getDeclaredFields();
        assertEquals(2, declaredFields.length, "2 fields (credits and debits) should be available in " + classToFind);

        for (final Field field : declaredFields) {
            assertTrue(isPrivate(field), field.getName() + " should be declared private");
            assertEquals(float[].class, field.getType(), field.getName() + " should be of type 'float[]'");
            assertTrue(fieldNames.contains(field.getName()), field.getName() + " is not a valid name. It should be either 'credits' or 'debits'");
        }
    }
}
