package org.mockito.util;

import org.junit.Before;
import org.mockito.internal.*;

public class RequiresValidState {

    @Before public void requiresValidState() {
        MockitoState.instance().validateState();
    }
}
