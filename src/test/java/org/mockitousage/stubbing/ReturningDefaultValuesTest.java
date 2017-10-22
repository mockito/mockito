/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class ReturningDefaultValuesTest extends TestBase {

    @Mock private IMethods mock;

    @Test
    public void shouldReturnAllKindsOfPrimitives() throws Exception {
        assertEquals((byte) 0, mock.byteReturningMethod());
        assertEquals((short) 0, mock.shortReturningMethod());
        assertEquals(0, mock.intReturningMethod());
        assertEquals(0L, mock.longReturningMethod());
        assertEquals(0.0F, mock.floatReturningMethod(), 0.0F);
        assertEquals(0.0D, mock.doubleReturningMethod(), 0.0D);
        assertEquals((char) 0, mock.charReturningMethod());
        assertEquals(false, mock.booleanReturningMethod());
        assertEquals(null, mock.objectReturningMethod());
    }

    @Test
    public void shouldReturnTheSameValuesForWrapperClasses() throws Exception {
        assertEquals(new Byte((byte) 0), mock.byteObjectReturningMethod());
        assertEquals(new Short((short) 0), mock.shortObjectReturningMethod());
        assertEquals(new Integer(0), mock.integerReturningMethod());
        assertEquals(new Long(0L), mock.longObjectReturningMethod());
        assertEquals(new Float(0.0F), mock.floatObjectReturningMethod(), 0.0F);
        assertEquals(new Double(0.0D), mock.doubleObjectReturningMethod(), 0.0D);
        assertEquals(new Character((char) 0), mock.charObjectReturningMethod());
        assertEquals(new Boolean(false), mock.booleanObjectReturningMethod());
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
        List<?> list() {
            return null;
        }

        LinkedList<?> linkedList() {
            return null;
        }

        Map<?, ?> map() {
            return null;
        }

        HashSet<?> hashSet() {
            return null;
        }
    }
}
