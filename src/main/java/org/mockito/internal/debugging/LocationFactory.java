/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.invocation.Location;

public final class LocationFactory {
    private static final Factory factory = createLocationFactory();

    private LocationFactory() {}

    public static Location create() {
        return create(false);
    }

    public static Location create(boolean inline) {
        return factory.create(inline);
    }

    private interface Factory {
        Location create(boolean inline);
    }

    private static Factory createLocationFactory() {
        try {
            Class.forName("java.lang.StackWalker");
            return new Java9PlusLocationFactory();
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

    private static final class Java9PlusLocationFactory implements Factory {

        @Override
        public Location create(boolean inline) {
            return new Java9PlusLocationImpl(inline);
        }
    }
}
