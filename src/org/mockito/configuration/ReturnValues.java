package org.mockito.configuration;

import org.mockito.internal.invocation.InvocationOnMock;

public interface ReturnValues {

    Object valueFor(InvocationOnMock invocation);

}