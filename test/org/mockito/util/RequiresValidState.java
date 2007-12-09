package org.mockito.util;

import org.junit.Before;
import org.mockito.internal.*;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests
 */
public class RequiresValidState {

    @Before
    public void requiresValidState() {
        MockitoState.instance().validateState();
    }
}
