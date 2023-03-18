/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;
import org.mockito.Mockito;

public class FinalClassMockingTest {

    @SuppressWarnings("CheckReturnValue")
    @Test
    public void no_exception_while_mocking_final_class() throws Exception {
        Mockito.mock(FinalClass.class);
    }

    private static final class FinalClass {

    }

}
