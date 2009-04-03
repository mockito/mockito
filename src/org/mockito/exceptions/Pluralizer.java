package org.mockito.exceptions;

public class Pluralizer {

    public static String pluralize(int number) {
        return number == 1 ? "1 time" : number + " times";
    }
}
