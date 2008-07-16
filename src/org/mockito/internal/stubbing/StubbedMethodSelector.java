package org.mockito.internal.stubbing;

public interface StubbedMethodSelector {

    <T> T when(T mock);
}