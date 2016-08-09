package org.mockito.internal.invocation;

import org.mockito.invocation.Invocation;

/**
 * TODO SF! also javadoc + invocation exposes too much?
 */
public interface Stubbing {

    Invocation getInvocation();

    boolean wasUsed();
}
