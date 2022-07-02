/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import java.util.concurrent.Callable;

public final class ExceptionHider {

    private ExceptionHider() {
        // utility class
    }

    public static <T> T wrap(Callable<T> provider) {
        try {
            return provider.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
