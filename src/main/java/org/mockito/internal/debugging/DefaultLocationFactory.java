/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.invocation.Location;
import org.mockito.plugins.LocationFactory;

public final class DefaultLocationFactory implements LocationFactory {
    private final Factory factory = createLocationFactory();

    public Location create() {
        return create(false);
    }

    public Location create(boolean inline) {
        return factory.create(inline);
    }

    private interface Factory {
        Location create(boolean inline);
    }

    private static Factory createLocationFactory() {
        try {
            // On some platforms, like Android, the StackWalker APIs may not be
            // available, in this case we have to fallback to Java 8 style of stack
            // traversing.
            Class.forName("java.lang.StackWalker");
            return new StackWalkerLocationFactory();
        } catch (ClassNotFoundException e) {
            return new Java8LocationFactory();
        }
    }

    private static final class Java8LocationFactory implements Factory {
        @Override
        public Location create(boolean inline) {
            return new Java8LocationImpl(new Throwable(), inline);
        }
    }

    private static final class StackWalkerLocationFactory implements Factory {
        @Override
        public Location create(boolean inline) {
            return new LocationImpl(inline);
        }
    }
}
