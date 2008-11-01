package org.mockito.internal.progress;

public class SequenceNumber {
    
    private static int sequenceNumber = 1;

    public static synchronized int next() {
        return sequenceNumber++;
    }
}