package org.mockito.configuration;

import org.mockito.internal.invocation.Invocation;

public interface ReturnValues {

    Object valueFor(Invocation invocation);

}