/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

/**
 * Describes the location of something in the source code.
 */
public interface Location {

    /**
     * Human readable location in the source code, see {@link Invocation#getLocation()}
     *
     * @return location
     */
    @Override
    String toString();

    /**
     * Source file of this location
     *
     * @return source file
     * @since 2.24.6
     */
    String getSourceFile();
}
