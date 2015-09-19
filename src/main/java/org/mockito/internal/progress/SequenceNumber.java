/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

public class SequenceNumber {
    
    private static int sequenceNumber = 1;

    public static synchronized int next() {
        return sequenceNumber++;
    }
}