package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import org.junit.Test;

public class WhiteboxTest {

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