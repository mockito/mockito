/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public interface MockitoDebugger {

    //Prints all interactions with mock. Also prints stubbing information.
    //You can put it in your 'tearDown' method
    String printInvocations(Object ... mocks);

}