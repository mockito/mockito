package org.mockito;

public interface MockitoDebugger {

    //Prints all interactions with mock. Also prints stubbing information.
    //You can put it in your 'tearDown' method
    String printInvocations(Object ... mocks);

}