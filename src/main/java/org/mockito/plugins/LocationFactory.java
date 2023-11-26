/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.invocation.Location;

/**
 * LocationFactory, which is used to create a {@link Location} to point at source code.
 */
public interface LocationFactory {

    /**
     * Creates a {@link Location}, which contains the source code line, which triggert the creation.
     * Note that the method is not directly called and if you implement this method,
     * you should filter out any mockito calls.
     *
     * @return the Location in source where this method was called.
     */
    Location create();

    /**
     * Creates a {@link Location}, which contains the source code line, which triggert the creation.
     * Note that the method is not directly called and if you implement this method,
     * you should filter out any mockito calls.
     *
     * @param inline whether it is inline or not.
     * @return the Location in source where this method was called.
     */
    Location create(boolean inline);
}
