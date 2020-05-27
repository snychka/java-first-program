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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.ReflectionUtils.*;

public class Module5_Test {
    private final String classToFind = "Finance";

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
    }

    @Test
    public void m5_04_testValidateCommandArgumentsExistence() {
        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();
        final String methodName = "validateCommandArguments";

        try {
            Method method = aClass.getDeclaredMethod(methodName, String[].class);
            assertTrue(isPrivate(method), methodName + " must be declared 'private'");
            assertTrue(isStatic(method), methodName + " must be declared 'static'");
            assertEquals(boolean.class, method.getReturnType(), methodName + " must have a 'boolean' return type");

        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 1 parameter of type 'String[]'");
        }
    }

    @Test
    public void m5_05_testValidateCommandArgumentsCorrectness() throws InvocationTargetException, IllegalAccessException {
        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " should be present");
        assertEquals(classToFind, maybeClass.get().getCanonicalName());

        final Class<?> aClass = maybeClass.get();
        final String methodName = "validateCommandArguments";

        try {
            Method aMethod = aClass.getDeclaredMethod(methodName, String[].class);
            aMethod.setAccessible(true);
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"bestLoanRates"});
                assertTrue(actual, methodName + " should have returned 'true' for bestLoanRates with no additional arguments");
            }
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"bestLoanRates", "1"});
                assertFalse(actual, methodName + " should have returned 'false' for bestLoanRates with 1 additional argument");
            }
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"savingsCalculator", "1"});
                assertFalse(actual, methodName + " should have returned 'false' for savingsCalculator with 1 additional argument");
            }
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"savingsCalculator", "1", "2"});
                assertTrue(actual, methodName + " should have returned 'true' for savingsCalculator with 2 additional arguments");
            }
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"mortgageCalculator", "1", "2"});
                assertFalse(actual, methodName + " should have returned 'false' for mortgageCalculator with 2 additional arguments");
            }
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"mortgageCalculator", "1", "2", "3"});
                assertTrue(actual, methodName + " should have returned 'true' for mortgageCalculator with 3 additional arguments");
            }
            {
                boolean actual = (boolean) aMethod.invoke(null, (Object) new String[]{"heightCalculator"});
                assertFalse(actual, methodName + " should have returned 'false' for unknown app name 'heightCalculator'");
            }
        } catch (NoSuchMethodException e) {
            fail("Can't find a method with name " + methodName + " in class " + classToFind + " with 1 parameter of type 'String[]'");
        }
    }

    @Test
    public void m5_06_testExecuteCommandExistence() {
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

    @Test
    public void m5_07_testExecuteCommandExistenceForCorrectness() {
        final String methodName = "executeCommand";

        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must exist");
        final Class<?> aClass = maybeClass.get();

        final Method[] methods = aClass.getDeclaredMethods();
        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");

        final Method method = filteredMethod.get(0);
        {
            final String credits = "10.0,20.0";
            final String debits = "5.0,20.0";

            invokeMethod(method, null, "savingsCalculator", new String[]{credits, debits});

            List<String> consoleOutputs = Arrays.asList(testOut.toString().split("\n"));

            assertEquals(2, consoleOutputs.size(), "For case SAVINGS_CALCULATOR, " + methodName + " should print 2 statements on the console. One for 'Finding your net savings ...' and another one should be the output from the SavingsCalculator");
            assertEquals("Finding your net savings ...", consoleOutputs.get(0));
            assertTrue(consoleOutputs.get(1).startsWith("Net Savings = 5.0, remaining days in month = "), "For case SAVINGS_CALCULATOR, " + methodName + " should have printed an output similar to 'Net Savings = 51.0, remaining days in month = '");
            setUpOutput();
        }
        {
            final String loanAmount = "264000";
            final String termInYears = "30";
            final String annualRate = "3.74";
            invokeMethod(method, null, "mortgageCalculator", new String[]{loanAmount, termInYears, annualRate});

            List<String> consoleOutputs = Arrays.asList(testOut.toString().split("\n"));

            assertEquals(2, consoleOutputs.size(), "For case MORTGAGE_CALCULATOR, " + methodName + " should print 2 statements on the console. One for 'Finding your monthly payment ...' and another one should be the output from the MortgageCalculator");
            assertEquals("Finding your monthly payment ...", consoleOutputs.get(0));
            assertEquals("monthlyPayment: 1221.14", consoleOutputs.get(1));
            setUpOutput();
        }
        {
            final String name = "H2";
            final int loanTermInYears = 20;
            final String testString = name + "\n" + loanTermInYears;
            provideInput(testString);

            invokeMethod(method, null, "bestLoanRates", new String[]{});
            List<String> consoleOutputs = Arrays.asList(testOut.toString().split("\n"));

            assertEquals(5, consoleOutputs.size(), "For case BEST_LOAN_RATES, There must be 4 statements on console - 1 for asking name, 1 for printing name back, 1 for asking loan term, 1 for printing no available rates for term (strictly in this order!)");

            assertEquals("Finding best loan rates ...", consoleOutputs.get(0));
            assertEquals("Enter your name", consoleOutputs.get(1));
            assertEquals("Hello " + name, consoleOutputs.get(2));

            assertEquals("Enter the loan term (in years)", consoleOutputs.get(3));
            assertEquals("No available rates for term: " + loanTermInYears + " years", consoleOutputs.get(4));
        }
    }

    @Test
    public void m5_08_testFinanceMainMethodExists() {
        final String methodName = "main";

        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must exist");
        final Class<?> aClass = maybeClass.get();

        final Method[] methods = aClass.getDeclaredMethods();
        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");

        final Method method = filteredMethod.get(0);
        assertTrue(isPublic(method), methodName + " must be declared as 'public'");
        assertTrue(isStatic(method), methodName + " must be declared as 'static'");
        assertEquals(void.class, method.getReturnType(), methodName + " method must return a value of type 'void'");

        final Class<?>[] parameterTypes = method.getParameterTypes();
        assertEquals(1, parameterTypes.length, methodName + " must accept 1 parameter.");
        assertEquals(String[].class, parameterTypes[0], methodName + " must accept only 1 parameter of type 'String[]'");
    }
    @Test
    public void m5_09_testFinanceMainMethodWorksCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String methodName = "main";

        final Optional<Class<?>> maybeClass = getFinanceClass();
        assertTrue(maybeClass.isPresent(), classToFind + " must exist");
        final Class<?> aClass = maybeClass.get();

        final Method[] methods = aClass.getDeclaredMethods();
        final List<Method> filteredMethod = Arrays.stream(methods).filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        assertEquals(1, filteredMethod.size(), classToFind + " should contain a method called '" + methodName + "'");



        Method method = aClass.getMethod("main", String[].class);
        {
            String command = "LaunchRocketToMoon";
            method.invoke(null, (Object) new String[]{command});
            assertEquals(command + ": command not found" + "\n", testOut.toString());
        }
        {
            setUpOutput();
            final String loanAmount = "264000";
            final String termInYears = "30";
            method.invoke(null, (Object) new String[]{"mortgageCalculator", loanAmount, termInYears});
            assertEquals("usage: mortgageCalculator <loanAmount> <termInYears> <annualRate>" + "\n", testOut.toString());
        }
        {
            setUpOutput();
            final String loanAmount = "264000";
            final String termInYears = "30";
            final String annualRate = "3.74";

            method.invoke(null, (Object) new String[]{"mortgageCalculator", loanAmount, termInYears, annualRate});
            List<String> consoleOutputs = Arrays.asList(testOut.toString().split("\n"));

            assertEquals(2, consoleOutputs.size(), "For case MORTGAGE_CALCULATOR, " + methodName + " should print 2 statements on the console. One for 'Finding your monthly payment ...' and another one should be the output from the MortgageCalculator");
            assertEquals("Finding your monthly payment ...", consoleOutputs.get(0));
            assertEquals("monthlyPayment: 1221.14", consoleOutputs.get(1));
        }
    }
}
