package com.h2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void m02_01_testSavingsCalculatorExists() {
        Optional<Class<?>> maybeClass = getSavingsClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must exist");
    }
}
