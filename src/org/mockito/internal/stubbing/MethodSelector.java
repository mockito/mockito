package org.mockito.internal.stubbing;

public interface MethodSelector {

    <T> T when(T mock);
}