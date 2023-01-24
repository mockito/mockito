/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.invocation.Location;

public final class LocationFactory {
    private LocationFactory() {}

    public static Location create() {
        return create(false);
    }

    public static Location create(boolean inline) {
        return new LocationImpl(inline);
    }
}
