package org.mockito;

import org.junit.Before;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests
 */
public class RequiresValidState {

    @Before
    public void requiresValidState() {
        Mockito.mockitoState.validateState();
    }
}