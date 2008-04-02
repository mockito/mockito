/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class ReturningDefaultValuesTest extends TestBase {

    @Test
    public void shouldReturnAllKindsOfPrimitives() throws Exception {
        IMethods mock = Mockito.mock(IMethods.class);

        //this is mainly to prove that cglib-enchanted-thing works properly 
        //and returns primitive value rather than throw NullPointerException
        //If we used java.lang.reflect.Proxy NullPointerException will bang
        assertEquals((byte) 0, mock.byteReturningMethod(12));
        assertEquals((short) 0, mock.shortReturningMethod(12));
        assertEquals(0, mock.intReturningMethod(12));
        assertEquals(0L, mock.intReturningMethod(12));
        assertEquals(0.0F, mock.floatReturningMethod(12), 0.0F);
        assertEquals(0.0D, mock.doubleReturningMethod(12), 0.0D);
        assertEquals((char) 0, mock.charReturningMethod(12));
        assertEquals(false, mock.booleanReturningMethod(12));
        assertEquals(null, mock.objectReturningMethod(12));
    }
    
    @Test 
    public void shouldReturnEmptyCollections() {
        CollectionsServer mock = Mockito.mock(CollectionsServer.class);
        
        assertTrue(mock.list().isEmpty());
        assertTrue(mock.linkedList().isEmpty());
        assertTrue(mock.map().isEmpty());
        assertTrue(mock.hashSet().isEmpty());
    }
    
    @Test 
    public void shouldReturnMutableEmptyCollection() {
        CollectionsServer mock = Mockito.mock(CollectionsServer.class);
        
        List list = mock.list();
        list.add("test");
       
        assertTrue(mock.list().isEmpty());
    }
    
    private class CollectionsServer {
        List list() {
            return null;
        }

        LinkedList linkedList() {
            return null;
        }

        Map map() {
            return null;
        }

        java.util.HashSet hashSet() {
            return null;
        }
    }
}