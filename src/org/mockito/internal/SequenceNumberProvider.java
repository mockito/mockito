package org.mockito.internal;

public class SequenceNumberProvider {

    private int sequence = 1;
    
    public Integer sequenceNumber() {
        return sequence++;
    }
}
