/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

public class DontThrow extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final DontThrow DONT_THROW = new DontThrow();
}
