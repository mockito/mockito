package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class WhiteboxTest extends TestBase {

    @Test
    public void shouldSetInternalStateOnHierarchy() {
        //given
        DummyClassForTests dummy = new DummyClassForTests();
        //when
        Whitebox.setInternalState(dummy, "somePrivateField", "cool!");
        //then
        Object internalState = org.powermock.reflect.Whitebox.getInternalState(dummy, "somePrivateField");
        assertEquals("cool!", internalState);
    }

    @Test
    public void shouldGetInternalStateFromHierarchy() {
        //given
        DummyClassForTests dummy = new DummyClassForTests();
        org.powermock.reflect.Whitebox.setInternalState(dummy, "somePrivateField", "boo!");
        //when
        Object internalState = Whitebox.getInternalState(dummy, "somePrivateField");
        //then
        assertEquals("boo!", internalState);
    }
}