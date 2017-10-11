package org.mockito.internal.util;

import org.mockito.invocation.MockHandler;

/**
 * Represents Mockito mock object, gives access to {@link MockHandler}
 *
 * TODO SF (perf tweak) in our codebase we call mockMaker.getHandler() multiple times unnecessarily
 * This is not ideal because getHandler() can be expensive (reflective calls inside mock maker)
 * The frequent pattern in the codebase is: 1) isMock(mock) 2) getMockHandler(mock)
 * We could replace it with using getMockitoMock()
 * Let's refactor the codebase and use new getMockitoMock() in all relevant places.
 * Potentially we could also move other methods to MockitoMock, some other candidates:
 *  getInvocationContainer, isSpy, etc.
 * The goal is to avoid unnecessary calls to mockMaker.getHandler
 */
public class MockitoMock {

    private final Object mock;
    private final MockHandler handler;

    /**
     * @param mock
     * @param handler ok to pass null, it means it is not a mock object
     */
    MockitoMock(Object mock, MockHandler handler) {
        this.mock = mock;
        this.handler = handler;
    }

    public boolean isMock() {
        return handler != null;
    }

    public MockHandler getHandler() {
        return handler;
    }

    public Object getMock() {
        return mock;
    }
}
