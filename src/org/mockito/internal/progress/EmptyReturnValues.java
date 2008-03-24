/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

public class EmptyReturnValues {

    public byte returnZero() {
        return 0;
    }

    public char returnChar() {
        return 0;
    }

    public <T> T returnNull() {
        return null;
    }

    public boolean returnFalse() {
        return false;
    }
}
