package org.mockito.invocation;

/**
 * Created by sfaber on 8/5/16.
 */
public interface Stubbing {

    //TODO 384 - Invocation exposes too much
    //TODO 384 javadoc
    Invocation getInvocation();

    boolean wasUsed();
}
