package com.h2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.ReflectionUtils.isPrivate;
import static org.junit.platform.commons.util.ReflectionUtils.tryToLoadClass;

public class Module04_Test {
    private final String classToFind = "com.h2.MortgageCalculator";
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

    public Optional<Class<?>> getMortgageClass() {
        return getClass(classToFind);
    }

    @Test
    public void m04_01_testMortgageCalculatorExists() {
        Optional<Class<?>> maybeClass = getMortgageClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must be created.");
    }

    @Test
    public void m04_02_testExistenceOfPrivateFields() {
        final Optional<Class<?>> maybeMortgageCalculator = getMortgageClass();
        assertTrue(maybeMortgageCalculator.isPresent(), classToFind + " must exist");
        final Class<?> mortgageCalculator = maybeMortgageCalculator.get();
        final Map<String, Class<?>> expectedFieldsToClass = Map.of(
                "loanAmount", long.class,
                "termInYears", int.class,
                "annualRate", float.class,
                "monthlyPayment", double.class
        );

        final Field[] declaredFields = mortgageCalculator.getDeclaredFields();
        assertEquals(4, declaredFields.length, "4 fields (loanAmount, termInYears, annualRate, monthlyPayment) should be available in " + classToFind);


        final Map<String, Class<?>> actualFieldsToClass = new HashMap<>();

        for (final Field field : declaredFields) {
            assertTrue(expectedFieldsToClass.containsKey(field.getName()), field.getName() + " is not a valid field name. It should be among (loanAmount, termInYears, annualRate, monthlyPayment)");
            assertTrue(isPrivate(field), field.getName() + " should be declared 'private'");
            actualFieldsToClass.put(field.getName(), field.getType());
        }

        expectedFieldsToClass.entrySet()
                .forEach(e -> {
                    assertEquals(e.getValue(), actualFieldsToClass.get(e.getKey()), e.getKey() + " must be of type " + e.getValue());
                });

    }
}
