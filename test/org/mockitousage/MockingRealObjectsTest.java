/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockingRealObjectsTest extends TestBase {

    List list = new LinkedList();
    List spy = Mockito.spy(list);
    
    @Test
    public void shouldVerify() {
        spy.add("one");
        spy.add("two");
        
        assertEquals("one", spy.get(0));
        assertEquals("two", spy.get(1));
        
        verify(spy).add("one");
        verify(spy).add("two");
    }
    
    @Test
    public void shouldStub() {
        spy.add("one");
        stub(spy.get(0))
            .toReturn("1")
            .toReturn("1 again");
               
        assertEquals("1", spy.get(0));
        assertEquals("1 again", spy.get(0));
        assertEquals("one", spy.iterator().next());
        
        assertEquals(1, spy.size());
    }
    
    @Test
    public void shouldStubVoid() {
        stubVoid(spy)
            .toReturn()
            .toThrow(new RuntimeException())
            .on().clear();

        spy.add("one");
        spy.clear();
        try {
            spy.clear();
            fail();
        } catch (RuntimeException e) {}
            
        assertEquals(1, spy.size());
    }
    
    @Test
    public void shouldToString() {
        spy.add("foo");
        assertEquals("[foo]" , spy.toString());
    }
}