package com.h2;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.ReflectionUtils.*;

public class Module2_Test {
    private final String classToFind = "com.h2.App";

    public Optional<Class<?>> getAppClass() {
        Try<Class<?>> aClass = tryToLoadClass(classToFind);
        return aClass.toOptional();
    }

    @Test
    public void m02_01_testDoubleTheNumber() {
        for (int i = 1; i < 10; i++) {
            assertEquals(2 * i, App.doubleTheNumber(i), i + " should be " + 2 * i);
        }
    }

    @Test
    public void m02_02_assertPrivateMethodExistence() {
        final String methodName = "add";
        final Optional<Class<?>> maybeClass = getAppClass();
        assertTrue(maybeClass.isPresent());
        Class<?> aClass = maybeClass.get();
        Optional<Method> maybeMethod = findMethod(aClass, methodName, int[].class);
        assertTrue(maybeMethod.isPresent(), methodName + " should be present in " + aClass.getCanonicalName());

        final Method method = maybeMethod.get();
        assertTrue(isPrivate(method), methodName + " should be private");

        assertEquals(int.class, method.getReturnType(), methodName + " should return type should be 'int'");

        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length, methodName + " should have 1 parameter");
        assertEquals(int[].class, parameters[0].getType(), methodName + " parameter should be of type 'int[]'");

        assertTrue(isStatic(method), methodName + "should be static method");
        assertTrue(isPrivate(method), methodName + "should be private method");
    }

    @Test
    public void m02_03_assertPrivateMethodCorrectness() throws InvocationTargetException, IllegalAccessException {
        final String methodName = "add";
        final Optional<Class<?>> maybeClass = getAppClass();
        assertTrue(maybeClass.isPresent());
        Class<?> aClass = maybeClass.get();
        Optional<Method> maybeMethod = findMethod(aClass, methodName, int[].class);
        assertTrue(maybeMethod.isPresent(), methodName + " should be present in " + aClass.getCanonicalName());

        final Method method = maybeMethod.get();
        method.setAccessible(true);

        @SuppressWarnings("PrimitiveArrayArgumentToVarargsMethod")
        int sum123 = (int) method.invoke(aClass, new int[]{1, 2, 3});
        assertEquals(6, sum123, "1 + 2 + 3 should be 6, got " + sum123);
    }

}
