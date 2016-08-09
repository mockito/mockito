package org.mockito.internal.invocation;

import org.mockito.invocation.Invocation;

/**
 * Created by sfaber on 8/5/16.
 *
 * TODO 384 expose
 */
public interface Stubbing {

    //TODO 384 - Invocation exposes too much
    //TODO 384 javadoc
    Invocation getInvocation();

    boolean wasUsed();
}
