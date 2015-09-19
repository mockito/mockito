package org.mockito.release.util.operations;

import groovy.lang.Closure;

public class Operations {

    public static Operation toOperation(final Closure closure) {
        return new Operation() {
            public void perform() {
                closure.call();
            }
        };
    }
}
