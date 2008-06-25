package org.mockito.internal.util;

public class Logger {

    public static void log(String message) {
        System.out.println("T:"+Thread.currentThread().getId() + " " + message);
    }
}
