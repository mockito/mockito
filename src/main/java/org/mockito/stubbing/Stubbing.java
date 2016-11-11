/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.MockingDetails;
import org.mockito.invocation.Invocation;

/**
 * Stubbing declared on the mock object.
 * See detailed description including sample code and use cases see javadoc for {@link MockingDetails#getStubbings()}.
 *
 * @since 2.2.3
 */
public interface Stubbing {

    /**
     * Returns the method invocation that is stubbed.
     * E.g. in the example stubbing <code>when(mock.foo()).thenReturn(true)</code>
     * the invocation is <code>mock.foo()</code>.
     * <p>
     * The invocation instance is mutable.
     * It is not recommended to modify the state of invocation because it will influence mock behavior.
     * <p>
     * To understand how this method is useful, see the description at {@link MockingDetails#getStubbings()}.
     *
     * @since 2.2.3
     */
    Invocation getInvocation();

    /**
     * Informs if the stubbing was used
     * <p>
     * What does it mean 'used stubbing'?
     * Stubbing like <code>when(mock.foo()).thenReturn(true)</code> is considered used
     * when the method <code>mock.foo()</code> is actually invoked during the execution of code under test.
     * <p>
     * This method is used internally by Mockito to report and detect unused stubbings.
     * Unused stubbings are dead code and should be deleted to increase clarity of tests (see {@link org.mockito.quality.MockitoHint}.
     * <p>
     * To understand how this method is useful, see the description at {@link MockingDetails#getStubbings()}.
     *
     * @since 2.2.3
     */
    boolean wasUsed();
}
