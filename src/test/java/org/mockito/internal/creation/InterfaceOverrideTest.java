/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class InterfaceOverrideTest {

    public interface CloneableInterface extends Cloneable {

        CloneableInterface clone();
    }

    @Test
    public void inherit_public_method_from_interface() {
        CloneableInterface i = Mockito.mock(CloneableInterface.class);
        Mockito.when(i.clone()).thenReturn(i);

        assertEquals(i, i.clone());
    }
}
