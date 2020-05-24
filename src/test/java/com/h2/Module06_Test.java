package com.h2;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.ReflectionUtils.*;

public class Module06_Test {
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
}
