package org.mockito;

public interface Strictly {

    <T> T verify(T mock);

    //TODO get rid of interface with int
    <T> T verify(T mock, int expectedNumberOfInvocations);

    void verifyNoMoreInteractions();

}