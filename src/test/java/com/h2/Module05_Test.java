package com.h2;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.ReflectionUtils.*;

public class Module05_Test {
    private final String classToFind = "Finance";

    public Optional<Class<?>> getFinanceClass() {
        Try<Class<?>> aClass = tryToLoadClass(classToFind);
        return aClass.toOptional();
    }

    @Test
    public void m5_01_assertFinanceClassExistence() {
        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());
    }

    @Test
    public void m05_02_testCommandConstantFields() throws IllegalAccessException {
        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();

        final Set<String> fieldNames = Set.of("BEST_LOAN_RATES", "SAVINGS_CALCULATOR", "MORTGAGE_CALCULATOR");
        for (String fieldName : fieldNames) {
            try {
                Field field = aClass.getDeclaredField(fieldName);
                assertTrue(isPublic(field), fieldName + " must be declared 'public'");
                assertTrue(isStatic(field), fieldName + " must be declared 'static'");
                assertTrue(isFinal(field), fieldName + " must be declared 'final'");

                switch (fieldName) {
                    case "BEST_LOAN_RATES":
                        assertEquals("bestLoanRates", field.get(null), "BEST_LOAN_RATES must have a value of 'bestLoanRates'");
                        break;
                    case "SAVINGS_CALCULATOR":
                        assertEquals("savingsCalculator", field.get(null), "SAVINGS_CALCULATOR must have a value of 'savingsCalculator'");
                        break;
                    case "MORTGAGE_CALCULATOR":
                        assertEquals("mortgageCalculator", field.get(null), "MORTGAGE_CALCULATOR must have a value of 'mortgageCalculator'");
                        break;
                }

            } catch (NoSuchFieldException e) {
                fail("Cannot find a field called " + fieldName);
            }
        }
    }

    @Test
    public void m5_03_testCommandsToUsage() throws IllegalAccessException {
        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();

        final String fieldName = "commandsToUsage";
        try {
            Field field = aClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            assertEquals(Map.class, field.getType(), fieldName + " must be of type 'Map<String, String>'");

            final Map<String, String> expected = Map.of(
                    "bestLoanRates", "usage: bestLoanRates",
                    "savingsCalculator", "usage: savingsCalculator <credits separated by ','> <debits separated by ','>",
                    "mortgageCalculator", "usage: mortgageCalculator <loanAmount> <termInYears> <annualRate>"
            );

            @SuppressWarnings("unchecked")
            Map<String, String> commandsToUsage = (Map<String, String>) field.get(null);
            assertEquals(3, commandsToUsage.size(), fieldName + " should have 3 entries");
            expected.forEach((key, value) -> {
                assertTrue(commandsToUsage.containsKey(key), key + " must be present in 'commandsToUsage'");
                assertEquals(value, commandsToUsage.get(key), key + " must have a value of - " + value);
            });

        } catch (NoSuchFieldException e) {
            fail("Cannot find a field called " + fieldName + " in class " + classToFind);
        }
        /*
         * 1. Existence of field
         * 2. isPublic, isFinal, isStatic, isMap (type)
         * 3. Has 3 entries
         * 4. Test all entries
         */
    }

    @Test
    public void m5_04_testExecuteCommandExistence() {
        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();
        final String methodName = "executeCommand";

        try {
            Method method = aClass.getDeclaredMethod(methodName, String.class, String[].class);
            assertTrue(isPrivate(method), methodName + " must be declared 'private'");
            assertTrue(isStatic(method), methodName + " must be declared 'static'");
            assertEquals(void.class, method.getReturnType(), methodName + " must have a 'void' return type");

        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 2 parameters, first with type 'String', second with type 'String[]'");
        }
    }
}
