/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Verifies #688.
 */
public class EnabledMockingInterfaceCloneMethodTest {

    @Test
    public void ensure_mocking_interface_clone_method_doesnot_throw_IllegalAccessError() {
        CloneableInterface ci = Mockito.mock(CloneableInterface.class);
        Mockito.when(ci.clone()).thenReturn(ci);
    }

    interface CloneableInterface extends Cloneable {
        CloneableInterface clone();
    }
}
