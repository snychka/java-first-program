package com.h2;

public class Setup {
    public static void main(String[] args) {
        System.out.println("Hello");

    }

    public static int doubleTheNumber(int number) {
        // do it in module02
        // todo: fix the implementation
        // return -1;
        return 2 * number;
    }

    private static int privateMethod(int[] numbers) {
        var sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }
}
