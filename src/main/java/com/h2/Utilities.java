package com.h2;

public class Utilities {

    public static long getLongValue(String in) {
        long out = Long.MIN_VALUE;
        try {
            out = Long.parseLong(in);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(in + " cannot be converted into a 'long' value. Exiting program.");
        }
        return out;
    }

    public static int getIntValue(String in) {
        int out = Integer.MIN_VALUE;
        try {
            out = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(in + " cannot be converted into a 'int' value. Exiting program.");
        }
        return out;
    }

    public static float getFloatValue(String in) {
        float out = Float.MIN_VALUE;
        try {
            out = Float.parseFloat(in);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(in + " cannot be converted into a 'float' value. Exiting program.");
        }
        return out;
    }
}
