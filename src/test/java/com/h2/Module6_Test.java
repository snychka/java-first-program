package com.h2;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.ReflectionUtils.*;

public class Module6_Test {
    private final String classToFind = "com.h2.Utilities";


    public Optional<Class<?>> getUtilitiesClass() {
        Try<Class<?>> aClass = tryToLoadClass(classToFind);
        return aClass.toOptional();
    }

    @Test
    public void m6_01_assertFinanceClassExistence() {
        final Optional<Class<?>> maybeClass = getUtilitiesClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());
    }

    @Test
    public void m6_02_testGetLongValueExistence() {
        final String methodName = "getLongValue";
        final Optional<Class<?>> maybeClass = getUtilitiesClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();

        try {
            Method method = aClass.getDeclaredMethod(methodName, String.class);
            assertTrue(isPublic(method), methodName + " must be declared 'public'");
            assertTrue(isStatic(method), methodName + " must be declared 'static'");
            assertEquals(long.class, method.getReturnType(), methodName + " must have a 'long' return type");

        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 1 parameter of type 'String'");
        }
    }

    @Test
    public void m6_03_testGetLongValueCorrectness() throws IllegalAccessException, InvocationTargetException {
        final String methodName = "getLongValue";
        final Optional<Class<?>> maybeClass = getUtilitiesClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();
        try {
            Method method = aClass.getMethod(methodName, String.class);
            {
                long result = (long) method.invoke(null, "123");
                assertEquals(123L, result, methodName + " should convert '123' into '123L'");
            }
            {
                String input = "1.22";
                final InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(null, input));
                Throwable targetException = exception.getTargetException();
                assertEquals(IllegalArgumentException.class, targetException.getClass(), methodName + " should have thrown an instance of 'IllegalArgumentException'");
                assertEquals(input + " cannot be converted into a 'long' value. Exiting program.", targetException.getMessage());
            }
        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 1 parameter of type 'String'");
        }
    }

    @Test
    public void m6_04_testGetIntValueExistenceAndCorrectness() throws IllegalAccessException, InvocationTargetException {
        final String methodName = "getIntValue";
        final Optional<Class<?>> maybeClass = getUtilitiesClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();
        try {
            Method method = aClass.getMethod(methodName, String.class);
            assertTrue(isPublic(method), methodName + " must be declared 'public'");
            assertTrue(isStatic(method), methodName + " must be declared 'static'");
            assertEquals(int.class, method.getReturnType(), methodName + " must have a 'int' return type");

            {
                int result = (int) method.invoke(null, "100");
                assertEquals(100, result, methodName + " should convert '100' into '100'");
            }
            {
                String input = "hello";
                final InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(null, input));
                Throwable targetException = exception.getTargetException();
                assertEquals(IllegalArgumentException.class, targetException.getClass(), methodName + " should have thrown an instance of 'IllegalArgumentException'");
                assertEquals(input + " cannot be converted into a 'int' value. Exiting program.", targetException.getMessage());
            }
        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 1 parameter of type 'String'");
        }
    }

    @Test
    public void m6_05_testGetFloatValueExistenceAndCorrectness() throws IllegalAccessException, InvocationTargetException {
        final String methodName = "getFloatValue";
        final Optional<Class<?>> maybeClass = getUtilitiesClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();
        try {
            Method method = aClass.getMethod(methodName, String.class);
            assertTrue(isPublic(method), methodName + " must be declared 'public'");
            assertTrue(isStatic(method), methodName + " must be declared 'static'");
            assertEquals(float.class, method.getReturnType(), methodName + " must have a 'float' return type");

            {
                float result = (float) method.invoke(null, "123.12");
                assertEquals(123.12f, result, methodName + " should convert '123.12' into '123.12f'");
            }
            {
                String input = "hello";
                final InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(null, input));
                Throwable targetException = exception.getTargetException();
                assertEquals(IllegalArgumentException.class, targetException.getClass(), methodName + " should have thrown an instance of 'IllegalArgumentException'");
                assertEquals(input + " cannot be converted into a 'float' value. Exiting program.", targetException.getMessage());
            }
        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 1 parameter of type 'String'");
        }
    }

    @Test
    public void m6_06_testMainMethodPrintsCorrectMortgageAmount() throws NoSuchMethodException {
        final String methodName = "main";

        final Optional<Class<?>> maybeMortgageCalculator = Module4_Test.getMortgageClass();
        assertTrue(maybeMortgageCalculator.isPresent(), classToFind + " must exist");
        final Class<?> mortgageCalculator = maybeMortgageCalculator.get();

        final Method[] methods = mortgageCalculator.getDeclaredMethods();

        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");

        final String loanAmount = "264000";
        final String termInYears = "30";
        final String annualRate = "3.74";
        String input = "hello";

        Method method = mortgageCalculator.getMethod("main", String[].class);
        {
            final InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(null, (Object) new String[]{input, termInYears, annualRate}));
            Throwable targetException = exception.getTargetException();
            assertEquals(IllegalArgumentException.class, targetException.getClass(), methodName + " should have thrown an instance of 'IllegalArgumentException'");
            assertEquals(input + " cannot be converted into a 'long' value. Exiting program.", targetException.getMessage());
        }
        {
            final InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(null, (Object) new String[]{loanAmount, input, annualRate}));
            Throwable targetException = exception.getTargetException();
            assertEquals(IllegalArgumentException.class, targetException.getClass(), methodName + " should have thrown an instance of 'IllegalArgumentException'");
            assertEquals(input + " cannot be converted into a 'int' value. Exiting program.", targetException.getMessage());
        }
        {
            final InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(null, (Object) new String[]{loanAmount, termInYears, input}));
            Throwable targetException = exception.getTargetException();
            assertEquals(IllegalArgumentException.class, targetException.getClass(), methodName + " should have thrown an instance of 'IllegalArgumentException'");
            assertEquals(input + " cannot be converted into a 'float' value. Exiting program.", targetException.getMessage());
        }
    }
}
