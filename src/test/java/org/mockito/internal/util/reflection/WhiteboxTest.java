/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import java.util.logging.Logger;

public class WhiteboxTest extends TestBase {

    @Mock
    Logger mockLogger;

    @Test
    public void private_state() {
        //given
        DummyClassForTests dummy = new DummyClassForTests();
        //when
        Whitebox.setInternalState(dummy, "somePrivateField", "cool!");
        //then
        Object internalState = Whitebox.getInternalState(dummy, "somePrivateField");
        assertEquals("cool!", internalState);
    }

    @Test
    public void private_static_final_state() {
        //when
        Whitebox.setInternalState(DummyParentClassForTests.class, "LOG", mockLogger);
        //then
        Object internalState = Whitebox.getInternalState(DummyParentClassForTests.class, "LOG");
        assertEquals(mockLogger, internalState);
    }
}