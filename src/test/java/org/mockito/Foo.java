package org.mockito;

public class Foo {

    private final String arg;

    public Foo(String arg) {
        this.arg = arg;
    }

    public static String staticMethod(String arg) {
        return "";
    }

    @Override
    public String toString() {
        return "foo:" + arg;
    }
}
