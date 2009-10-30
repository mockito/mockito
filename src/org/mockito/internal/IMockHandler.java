package org.mockito.internal;

import java.io.Serializable;

import org.mockito.internal.invocation.Invocation;

public interface IMockHandler extends Serializable {

    Object handle(Invocation invocation) throws Throwable;
}
