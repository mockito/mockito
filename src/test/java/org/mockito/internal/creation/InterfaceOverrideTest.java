package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;

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
