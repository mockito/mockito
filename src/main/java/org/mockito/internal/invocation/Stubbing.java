package org.mockito.internal.invocation;

import org.mockito.invocation.Invocation;

/**
 * TODO 542 javadoc + move to public package
 */
public interface Stubbing {

    Invocation getInvocation();

    boolean wasUsed();
}
