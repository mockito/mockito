/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.Stubbing;

import java.util.Collection;

public interface MockitoDebugger {

    //Prints all interactions with mock. Also prints stubbing information.
    //You can put it in your 'tearDown' method
    String printInvocations(Object ... mocks);

    //TODO 384 javadoc
    //TODO 384 inconsistent interface, change to Object ... mocks or iterable throughout
    Collection<Invocation> getInvocations(Iterable<Object> mocks);

    Collection<Stubbing> getStubbings(Iterable<Object> mocks);
}