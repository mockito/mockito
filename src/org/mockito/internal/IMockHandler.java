package org.mockito.internal;

import org.mockito.internal.invocation.Invocation;

public interface IMockHandler {

    Object handle(Invocation invocation) throws Throwable;
}
