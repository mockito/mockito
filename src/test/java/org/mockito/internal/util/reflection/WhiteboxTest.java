/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

public class WhiteboxTest extends TestBase {

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
}