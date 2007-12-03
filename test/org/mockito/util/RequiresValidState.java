package org.mockito.util;

import org.junit.Before;
import org.mockito.internal.MockitoState;

public class RequiresValidState {

    @Before public void requireValidState() {
        MockitoState.instance().validateState();
    }
}
