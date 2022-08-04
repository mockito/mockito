/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.invocation.Location;

import java.io.Serializable;

public final class LocationFactory {
    private static final String PROPERTY = "mockito.locationFactory.disable";
    private static final Factory factory;

    static {
        Factory lFactory;
        if (Boolean.getBoolean(PROPERTY)) {
            factory = unused -> NoLocation.INSTANCE;
        } else {
            try {
                Class.forName("java.lang.StackWalker");
                lFactory = new Java9PlusLocationFactory();
            } catch (ClassNotFoundException e) {
                lFactory = new Java8LocationFactory();
            }
            factory = lFactory;
        }
    }

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

    private static final class NoLocation implements Location, Serializable {
        private static final long serialVersionUID = -3807068467458099012L;
        private static final NoLocation INSTANCE = new NoLocation();

        private NoLocation() {}

        @Override
        public String getSourceFile() {
            return "<unknown source file>";
        }

        @Override
        public String toString() {
            return "-> at <<unknown line>>";
        }
    }
}
