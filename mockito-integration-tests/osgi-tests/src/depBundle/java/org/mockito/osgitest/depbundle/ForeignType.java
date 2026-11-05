/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.depbundle;

/**
 * A type that is only visible to the bundle defining the superclass under test.
 * The test bundle deliberately does not import this package.
 */
public class ForeignType {

    public String value() {
        return "foreign";
    }
}
